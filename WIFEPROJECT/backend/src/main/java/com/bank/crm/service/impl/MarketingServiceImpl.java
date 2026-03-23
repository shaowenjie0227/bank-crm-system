package com.bank.crm.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bank.crm.entity.CustomerInfo;
import com.bank.crm.entity.MarketingOpportunity;
import com.bank.crm.mapper.CustomerInfoMapper;
import com.bank.crm.mapper.MarketingMapper;
import com.bank.crm.service.MarketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 营销机会服务实现类
 */
@Service
public class MarketingServiceImpl extends ServiceImpl<MarketingMapper, MarketingOpportunity> implements MarketingService {

    @Autowired
    private CustomerInfoMapper customerInfoMapper;


    @Override
    public IPage<MarketingOpportunity> getMarketingList(Integer current, Integer size, String name, String source, String status) {
        Page<MarketingOpportunity> page = new Page<>(current, size);
        LambdaQueryWrapper<MarketingOpportunity> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(name)) {
            queryWrapper.like(MarketingOpportunity::getName, name);
        }
        if (StringUtils.hasText(source)) {
            queryWrapper.eq(MarketingOpportunity::getSource, source);
        }
        if (StringUtils.hasText(status)) {
            queryWrapper.eq(MarketingOpportunity::getStatus, status);
        }
        
        queryWrapper.orderByDesc(MarketingOpportunity::getCreateTime);
        
        return this.page(page, queryWrapper);
    }

    @Override
    public MarketingOpportunity getMarketingById(Integer id) {
        return this.getById(id);
    }

    @Override
    public MarketingOpportunity saveMarketing(MarketingOpportunity marketing) {
        marketing.setCreateTime(LocalDateTime.now());
        this.save(marketing);
        return marketing;
    }

    @Override
    public MarketingOpportunity updateMarketing(MarketingOpportunity marketing) {
        this.updateById(marketing);
        return marketing;
    }

    @Override
    public void deleteMarketing(Integer id) {
        this.removeById(id);
    }
    
    @Override
    public long count() {
        return super.count();
    }
    
    @Override
    public long countByCreateTimeBefore(java.time.LocalDate date) {
        LambdaQueryWrapper<MarketingOpportunity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.lt(MarketingOpportunity::getCreateTime, date.atStartOfDay());
        return this.count(queryWrapper);
    }
    
    @Override
    public List<MarketingOpportunity> getMarketingByCustomerId(Integer customerId) {
        // 首先通过客户ID获取客户信息
        CustomerInfo customer = customerInfoMapper.selectById(customerId);
        if (customer == null) {
            return null;
        }
        
        // 通过客户名称查询相关的营销机会
        LambdaQueryWrapper<MarketingOpportunity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MarketingOpportunity::getName, customer.getCustomerName());
        queryWrapper.orderByDesc(MarketingOpportunity::getCreateTime);
        
        return this.list(queryWrapper);
    }
}