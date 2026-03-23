package com.bank.crm.controller;

import com.bank.crm.vo.Result;
import com.bank.crm.entity.CustomerInfo;
import com.bank.crm.entity.MarketingOpportunity;
import com.bank.crm.entity.CustomerDevelopment;
import com.bank.crm.service.CustomerService;
import com.bank.crm.service.MarketingService;
import com.bank.crm.service.DevelopmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboards")
@CrossOrigin
public class DashboardController {

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private MarketingService marketingService;
    
    @Autowired
    private DevelopmentService developmentService;

    @GetMapping("/stats")
    public Result<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // 获取当前时间
            LocalDate now = LocalDate.now();
            LocalDate lastMonth = now.minusMonths(1);
            
            // 获取客户总数和上个月客户数
            long customerCount = customerService.count();
            long lastMonthCustomerCount = customerService.countByCreateTimeBefore(lastMonth);
            double customerChange = lastMonthCustomerCount > 0 ? 
                ((double)(customerCount - lastMonthCustomerCount) / lastMonthCustomerCount) * 100 : 0;
            
            // 获取营销机会总数和上个月营销机会数
            long opportunityCount = marketingService.count();
            long lastMonthOpportunityCount = marketingService.countByCreateTimeBefore(lastMonth);
            double opportunityChange = lastMonthOpportunityCount > 0 ? 
                ((double)(opportunityCount - lastMonthOpportunityCount) / lastMonthOpportunityCount) * 100 : 0;
            
            // 获取客户开发总数和上个月客户开发数
            long developmentCount = developmentService.count();
            long lastMonthDevelopmentCount = developmentService.countByCreateTimeBefore(lastMonth);
            double developmentChange = lastMonthDevelopmentCount > 0 ? 
                ((double)(developmentCount - lastMonthDevelopmentCount) / lastMonthDevelopmentCount) * 100 : 0;
            
            stats.put("customerCount", customerCount);
            stats.put("customerChange", Math.round(customerChange * 100.0) / 100.0); // 保留两位小数
            stats.put("opportunityCount", opportunityCount);
            stats.put("opportunityChange", Math.round(opportunityChange * 100.0) / 100.0);
            stats.put("developmentCount", developmentCount);
            stats.put("developmentChange", Math.round(developmentChange * 100.0) / 100.0);
            
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error("获取仪表板统计数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/customer-chart")
    public Result<List<Map<String, Object>>> getCustomerChartData(@RequestParam(required = false, defaultValue = "week") String period) {
        try {
            List<Map<String, Object>> chartData = new ArrayList<>();
            
            // 根据周期生成日期范围
            LocalDate now = LocalDate.now();
            LocalDate startDate;
            int days;
            
            if ("week".equals(period)) {
                startDate = now.minusDays(7);
                days = 7;
            } else if ("month".equals(period)) {
                startDate = now.minusMonths(1);
                days = 30;
            } else { // year
                startDate = now.minusYears(1);
                days = 365;
            }
            
            // 生成模拟数据
            Random random = new Random();
            for (int i = 0; i < days; i++) {
                LocalDate date = startDate.plusDays(i);
                Map<String, Object> dataPoint = new HashMap<>();
                dataPoint.put("date", date.toString());
                dataPoint.put("count", random.nextInt(10) + 1);
                chartData.add(dataPoint);
            }
            
            return Result.success(chartData);
        } catch (Exception e) {
            return Result.error("获取客户图表数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/conversion-chart")
    public Result<List<Map<String, Object>>> getConversionChartData() {
        try {
            // 获取营销机会数据
            List<Map<String, Object>> chartData = new ArrayList<>();
            
            // 模拟不同状态的营销机会数量
            Map<String, Integer> statusCount = new HashMap<>();
            statusCount.put("未开始", 35);
            statusCount.put("进行中", 25);
            statusCount.put("已完成", 30);
            statusCount.put("已失败", 10);
            
            // 转换为图表数据格式
            for (Map.Entry<String, Integer> entry : statusCount.entrySet()) {
                Map<String, Object> dataPoint = new HashMap<>();
                dataPoint.put("name", entry.getKey());
                dataPoint.put("value", entry.getValue());
                chartData.add(dataPoint);
            }
            
            return Result.success(chartData);
        } catch (Exception e) {
            return Result.error("获取转化率图表数据失败: " + e.getMessage());
        }
    }
}