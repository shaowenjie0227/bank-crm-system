package com.bank.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bank.crm.dto.RecommendationItemDTO;
import com.bank.crm.dto.RecommendationOverviewDTO;
import com.bank.crm.entity.CustomerInfo;
import com.bank.crm.entity.MarketingOpportunity;
import com.bank.crm.entity.RecommendationInteraction;
import com.bank.crm.entity.RecommendationResult;
import com.bank.crm.entity.User;
import com.bank.crm.mapper.CustomerInfoMapper;
import com.bank.crm.mapper.MarketingMapper;
import com.bank.crm.mapper.RecommendationInteractionMapper;
import com.bank.crm.mapper.RecommendationResultMapper;
import com.bank.crm.mapper.UserMapper;
import com.bank.crm.service.AuthService;
import com.bank.crm.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private static final String ALGORITHM_VERSION = "user-cf-v1";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private AuthService authService;

    @Autowired
    private RecommendationInteractionMapper recommendationInteractionMapper;

    @Autowired
    private RecommendationResultMapper recommendationResultMapper;

    @Autowired
    private CustomerInfoMapper customerInfoMapper;

    @Autowired
    private MarketingMapper marketingMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public RecommendationOverviewDTO getDashboardRecommendations() {
        User currentUser = authService.getCurrentUser();
        RecommendationOverviewDTO overview = new RecommendationOverviewDTO();
        overview.setUsername(currentUser.getUsername());
        overview.setAlgorithm("协同过滤推荐");

        List<RecommendationInteraction> interactions = recommendationInteractionMapper.selectList(
            new LambdaQueryWrapper<RecommendationInteraction>().orderByDesc(RecommendationInteraction::getCreateTime)
        );
        overview.setInteractionCount(interactions.size());

        List<CustomerInfo> customers = customerInfoMapper.selectList(
            new LambdaQueryWrapper<CustomerInfo>().orderByDesc(CustomerInfo::getUpdateTime).orderByDesc(CustomerInfo::getCreateTime)
        );
        List<MarketingOpportunity> opportunities = marketingMapper.selectList(
            new LambdaQueryWrapper<MarketingOpportunity>().orderByDesc(MarketingOpportunity::getUpdateTime).orderByDesc(MarketingOpportunity::getCreateTime)
        );

        Map<Integer, CustomerInfo> customerMap = customers.stream()
            .collect(Collectors.toMap(CustomerInfo::getId, customer -> customer, (left, right) -> left));
        Map<String, MarketingOpportunity> opportunityMap = opportunities.stream()
            .collect(Collectors.toMap(MarketingOpportunity::getName, opportunity -> opportunity, (left, right) -> left));
        List<Integer> interactionUserIds = interactions.stream()
            .map(RecommendationInteraction::getUserId)
            .distinct()
            .collect(Collectors.toList());
        Map<Integer, String> usernameMap = interactionUserIds.isEmpty()
            ? new HashMap<>()
            : userMapper.selectBatchIds(interactionUserIds).stream()
                .collect(Collectors.toMap(User::getId, User::getUsername, (left, right) -> left));

        Map<Integer, Map<Integer, Double>> userCustomerMatrix = buildUserCustomerMatrix(interactions);
        Map<Integer, Double> currentRatings = userCustomerMatrix.getOrDefault(currentUser.getId(), Collections.emptyMap());
        Map<Integer, Double> similarityMap = buildSimilarityMap(currentUser.getId(), currentRatings, userCustomerMatrix);

        List<RecommendationCandidate> candidates = buildCollaborativeCandidates(
            currentRatings,
            similarityMap,
            userCustomerMatrix,
            customerMap,
            opportunityMap,
            usernameMap
        );

        if (candidates.isEmpty()) {
            candidates = buildFallbackCandidates(currentUser, currentRatings, customers, opportunityMap);
        }

        List<RecommendationItemDTO> items = candidates.stream()
            .sorted(Comparator.comparing(RecommendationCandidate::getScore).reversed())
            .limit(4)
            .map(candidate -> toRecommendationItem(candidate, customerMap, opportunityMap))
            .collect(Collectors.toList());

        persistResults(currentUser.getId(), items);

        overview.setGeneratedAt(LocalDateTime.now().format(DATE_TIME_FORMATTER));
        overview.setSummary(buildSummary(items, currentRatings.size(), interactions.size()));
        overview.setRecommendations(items);
        return overview;
    }

    private Map<Integer, Map<Integer, Double>> buildUserCustomerMatrix(List<RecommendationInteraction> interactions) {
        Map<Integer, Map<Integer, Double>> matrix = new HashMap<>();
        for (RecommendationInteraction interaction : interactions) {
            matrix.computeIfAbsent(interaction.getUserId(), key -> new HashMap<>())
                .merge(interaction.getCustomerId(), interaction.getScore() == null ? 0D : interaction.getScore(), Double::sum);
        }
        return matrix;
    }

    private Map<Integer, Double> buildSimilarityMap(Integer currentUserId, Map<Integer, Double> currentRatings,
                                                    Map<Integer, Map<Integer, Double>> userCustomerMatrix) {
        Map<Integer, Double> similarityMap = new HashMap<>();
        for (Map.Entry<Integer, Map<Integer, Double>> entry : userCustomerMatrix.entrySet()) {
            Integer otherUserId = entry.getKey();
            if (otherUserId.equals(currentUserId)) {
                continue;
            }
            double similarity = cosineSimilarity(currentRatings, entry.getValue());
            if (similarity > 0) {
                similarityMap.put(otherUserId, similarity);
            }
        }
        return similarityMap;
    }

    private double cosineSimilarity(Map<Integer, Double> currentRatings, Map<Integer, Double> otherRatings) {
        if (CollectionUtils.isEmpty(currentRatings) || CollectionUtils.isEmpty(otherRatings)) {
            return 0D;
        }

        Set<Integer> sharedCustomers = new HashSet<>(currentRatings.keySet());
        sharedCustomers.retainAll(otherRatings.keySet());
        if (sharedCustomers.isEmpty()) {
            return 0D;
        }

        double numerator = 0D;
        double currentNorm = 0D;
        double otherNorm = 0D;

        for (Integer customerId : sharedCustomers) {
            double current = currentRatings.getOrDefault(customerId, 0D);
            double other = otherRatings.getOrDefault(customerId, 0D);
            numerator += current * other;
            currentNorm += current * current;
            otherNorm += other * other;
        }

        if (currentNorm == 0 || otherNorm == 0) {
            return 0D;
        }

        return numerator / (Math.sqrt(currentNorm) * Math.sqrt(otherNorm));
    }

    private List<RecommendationCandidate> buildCollaborativeCandidates(Map<Integer, Double> currentRatings,
                                                                       Map<Integer, Double> similarityMap,
                                                                       Map<Integer, Map<Integer, Double>> userCustomerMatrix,
                                                                       Map<Integer, CustomerInfo> customerMap,
                                                                       Map<String, MarketingOpportunity> opportunityMap,
                                                                       Map<Integer, String> usernameMap) {
        Map<Integer, RecommendationCandidate> candidateMap = new HashMap<>();

        for (Map.Entry<Integer, Double> similarityEntry : similarityMap.entrySet()) {
            Integer otherUserId = similarityEntry.getKey();
            Double similarity = similarityEntry.getValue();
            Map<Integer, Double> otherRatings = userCustomerMatrix.getOrDefault(otherUserId, Collections.emptyMap());
            String username = usernameMap.getOrDefault(otherUserId, "用户" + otherUserId);

            for (Map.Entry<Integer, Double> ratingEntry : otherRatings.entrySet()) {
                Integer customerId = ratingEntry.getKey();
                if (currentRatings.containsKey(customerId) || !customerMap.containsKey(customerId)) {
                    continue;
                }

                RecommendationCandidate candidate = candidateMap.computeIfAbsent(customerId, key -> new RecommendationCandidate(customerId));
                candidate.addContribution(similarity, ratingEntry.getValue(), username);
            }
        }

        return candidateMap.values().stream()
            .peek(candidate -> enrichCandidate(candidate, customerMap.get(candidate.getCustomerId()), opportunityMap))
            .filter(candidate -> candidate.getWeight() > 0)
            .sorted(Comparator.comparing(RecommendationCandidate::getScore).reversed())
            .collect(Collectors.toList());
    }

    private void enrichCandidate(RecommendationCandidate candidate, CustomerInfo customer,
                                 Map<String, MarketingOpportunity> opportunityMap) {
        if (customer == null) {
            return;
        }

        double levelBoost = getCustomerLevelBoost(customer.getCustomerLevel());
        MarketingOpportunity opportunity = opportunityMap.get(customer.getCustomerName());
        double opportunityBoost = opportunity == null ? 0D : 0.35D;
        candidate.finishScore(levelBoost + opportunityBoost);

        StringBuilder reason = new StringBuilder();
        if (!candidate.getSimilarUsers().isEmpty()) {
            reason.append("与你行为相似的客户经理 ")
                .append(String.join("、", candidate.getSimilarUsers()))
                .append(" 近期重点关注了该客户");
        } else {
            reason.append("该客户与当前重点跟进画像相似");
        }

        if (opportunity != null) {
            reason.append("，并且已有“").append(opportunity.getStage() == null ? "可推进机会" : opportunity.getStage()).append("”阶段机会可继续推进");
        }

        candidate.setReason(reason.toString());
    }

    private List<RecommendationCandidate> buildFallbackCandidates(User currentUser, Map<Integer, Double> currentRatings,
                                                                  List<CustomerInfo> customers,
                                                                  Map<String, MarketingOpportunity> opportunityMap) {
        return customers.stream()
            .filter(customer -> !currentRatings.containsKey(customer.getId()))
            .filter(customer -> customer.getCustomerManager() == null || !customer.getCustomerManager().equals(currentUser.getUsername()))
            .map(customer -> {
                RecommendationCandidate candidate = new RecommendationCandidate(customer.getId());
                double baseScore = 2.4D + getCustomerLevelBoost(customer.getCustomerLevel());
                if (opportunityMap.containsKey(customer.getCustomerName())) {
                    baseScore += 0.4D;
                }
                candidate.setScore(baseScore);
                candidate.setWeight(1D);
                candidate.setReason("当前用户历史行为样本较少，已按客户等级、机会成熟度和区域画像进行兜底推荐");
                return candidate;
            })
            .sorted(Comparator.comparing(RecommendationCandidate::getScore).reversed())
            .collect(Collectors.toList());
    }

    private RecommendationItemDTO toRecommendationItem(RecommendationCandidate candidate,
                                                       Map<Integer, CustomerInfo> customerMap,
                                                       Map<String, MarketingOpportunity> opportunityMap) {
        CustomerInfo customer = customerMap.get(candidate.getCustomerId());
        MarketingOpportunity opportunity = customer == null ? null : opportunityMap.get(customer.getCustomerName());

        RecommendationItemDTO item = new RecommendationItemDTO();
        if (customer != null) {
            item.setCustomerId(customer.getId());
            item.setCustomerName(customer.getCustomerName());
            item.setCustomerLevel(customer.getCustomerLevel());
            item.setCustomerIndustry(customer.getCustomerIndustry());
            item.setRegion(customer.getRegion());
        }
        if (opportunity != null) {
            item.setOpportunityId(opportunity.getId());
            item.setOpportunityTitle(opportunity.getDescription());
            item.setOpportunityStage(opportunity.getStage());
        }
        item.setScore(roundScore(candidate.getScore()));
        item.setReason(candidate.getReason());
        item.setSimilarUsers(new ArrayList<>(candidate.getSimilarUsers()));
        return item;
    }

    private void persistResults(Integer userId, List<RecommendationItemDTO> items) {
        recommendationResultMapper.delete(new LambdaQueryWrapper<RecommendationResult>()
            .eq(RecommendationResult::getUserId, userId));

        LocalDateTime now = LocalDateTime.now();
        for (RecommendationItemDTO item : items) {
            RecommendationResult result = new RecommendationResult()
                .setUserId(userId)
                .setCustomerId(item.getCustomerId())
                .setRecommendedOpportunityId(item.getOpportunityId())
                .setScore(item.getScore())
                .setReason(item.getReason())
                .setAlgorithmVersion(ALGORITHM_VERSION)
                .setGeneratedAt(now);
            recommendationResultMapper.insert(result);
        }
    }

    private String buildSummary(List<RecommendationItemDTO> items, int currentInteractionCount, int totalInteractionCount) {
        if (items.isEmpty()) {
            return "当前样本仍在积累中，建议先补充客户跟进记录后再生成个性化推荐。";
        }
        return String.format("基于 %d 条个人行为和 %d 条全局跟进样本，已为你筛选出 %d 个优先跟进客户。",
            currentInteractionCount, totalInteractionCount, items.size());
    }

    private double getCustomerLevelBoost(String customerLevel) {
        if (customerLevel == null) {
            return 0.1D;
        }
        if ("SVIP".equalsIgnoreCase(customerLevel) || "A级".equalsIgnoreCase(customerLevel)) {
            return 0.9D;
        }
        if ("VIP".equalsIgnoreCase(customerLevel) || "B级".equalsIgnoreCase(customerLevel)) {
            return 0.6D;
        }
        return 0.25D;
    }

    private double roundScore(double score) {
        return Math.round(score * 100.0D) / 100.0D;
    }

    private static class RecommendationCandidate {
        private final Integer customerId;
        private double weightedScore;
        private double weight;
        private double score;
        private String reason;
        private final LinkedHashSet<String> similarUsers = new LinkedHashSet<>();

        RecommendationCandidate(Integer customerId) {
            this.customerId = customerId;
        }

        Integer getCustomerId() {
            return customerId;
        }

        double getWeight() {
            return weight;
        }

        double getScore() {
            return score;
        }

        void setScore(double score) {
            this.score = score;
        }

        void setWeight(double weight) {
            this.weight = weight;
        }

        String getReason() {
            return reason;
        }

        void setReason(String reason) {
            this.reason = reason;
        }

        LinkedHashSet<String> getSimilarUsers() {
            return similarUsers;
        }

        void addContribution(double similarity, double rating, String username) {
            this.weightedScore += similarity * rating;
            this.weight += Math.abs(similarity);
            this.similarUsers.add(username);
        }

        void finishScore(double boost) {
            if (this.weight == 0) {
                this.score = 0D;
                return;
            }
            this.score = (this.weightedScore / this.weight) + boost;
        }
    }
}


