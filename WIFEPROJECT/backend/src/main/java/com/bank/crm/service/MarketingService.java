package com.bank.crm.service;

import com.bank.crm.entity.MarketingOpportunity;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 营销机会服务接口
 */
public interface MarketingService {
    
    /**
     * 获取营销机会列表
     */
    IPage<MarketingOpportunity> getMarketingList(Integer current, Integer size, String name, String source, String status);
    
    /**
     * 根据ID获取营销机会
     */
    MarketingOpportunity getMarketingById(Integer id);
    
    /**
     * 保存营销机会
     */
    MarketingOpportunity saveMarketing(MarketingOpportunity marketing);
    
    /**
     * 更新营销机会
     */
    MarketingOpportunity updateMarketing(MarketingOpportunity marketing);
    
    /**
     * 删除营销机会
     */
    void deleteMarketing(Integer id);
    
    /**
     * 获取营销机会总数
     */
    long count();
    
    /**
     * 获取指定时间前的营销机会数量
     */
    long countByCreateTimeBefore(java.time.LocalDate date);
    
    /**
     * 根据客户ID获取营销机会
     */
    List<MarketingOpportunity> getMarketingByCustomerId(Integer customerId);
}