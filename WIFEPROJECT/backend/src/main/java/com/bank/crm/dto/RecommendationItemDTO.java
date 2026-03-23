package com.bank.crm.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecommendationItemDTO {
    private Integer customerId;
    private String customerName;
    private String customerLevel;
    private String customerIndustry;
    private String region;
    private Integer opportunityId;
    private String opportunityTitle;
    private String opportunityStage;
    private Double score;
    private String reason;
    private List<String> similarUsers;
}
