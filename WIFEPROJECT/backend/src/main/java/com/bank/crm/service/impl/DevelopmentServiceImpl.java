package com.bank.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bank.crm.entity.CustomerDevelopment;
import com.bank.crm.entity.CustomerInfo;
import com.bank.crm.mapper.CustomerInfoMapper;
import com.bank.crm.mapper.DevelopmentMapper;
import com.bank.crm.service.DevelopmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 客户开发服务实现类
 */
@Service
public class DevelopmentServiceImpl extends ServiceImpl<DevelopmentMapper, CustomerDevelopment> implements DevelopmentService {

    @Autowired
    private CustomerInfoMapper customerInfoMapper;

    @Override
    public DevelopmentMapper getBaseMapper() {
        return super.getBaseMapper();
    }

    @Override
    public IPage<CustomerDevelopment> getDevelopmentList(Integer current, Integer size, String customerName, String progress, String status) {
        Page<CustomerDevelopment> page = new Page<>(current, size);
        LambdaQueryWrapper<CustomerDevelopment> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(customerName)) {
            queryWrapper.like(CustomerDevelopment::getName, customerName);
        }
        if (StringUtils.hasText(progress)) {
            queryWrapper.eq(CustomerDevelopment::getDevelopmentStage, progress);
        }
        if (StringUtils.hasText(status)) {
            queryWrapper.eq(CustomerDevelopment::getDevelopmentStatus, status);
        }
        
        queryWrapper.orderByDesc(CustomerDevelopment::getCreateTime);
        
        return this.page(page, queryWrapper);
    }

    @Override
    public CustomerDevelopment getDevelopmentById(Integer id) {
        return this.getById(id);
    }

    @Override
    public CustomerDevelopment saveDevelopment(CustomerDevelopment development) {
        // 确保ID为空，让数据库自动生成
        development.setId(null);
        // 保存实体，自动填充字段会由 MyMetaObjectHandler 处理
        boolean result = this.save(development);
        if (!result) {
            throw new RuntimeException("保存客户开发记录失败");
        }
        return development;
    }

    @Override
    public CustomerDevelopment updateDevelopment(CustomerDevelopment development) {
        boolean result = this.updateById(development);
        if (!result) {
            throw new RuntimeException("更新客户开发记录失败");
        }
        return development;
    }

    @Override
    public void deleteDevelopment(Integer id) {
        this.removeById(id);
    }
    
    @Override
    public long count() {
        return super.count();
    }
    
    @Override
    public long countByCreateTimeBefore(java.time.LocalDate date) {
        LambdaQueryWrapper<CustomerDevelopment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.lt(CustomerDevelopment::getCreateTime, date.atStartOfDay());
        return this.count(queryWrapper);
    }
    
    @Override
    public List<CustomerDevelopment> getDevelopmentByCustomerId(Integer customerId) {
        // 首先通过客户ID获取客户信息
        CustomerInfo customer = customerInfoMapper.selectById(customerId);
        if (customer == null) {
            return null;
        }
        
        // 通过客户名称查询相关的客户开发记录
        LambdaQueryWrapper<CustomerDevelopment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CustomerDevelopment::getName, customer.getCustomerName());
        queryWrapper.orderByDesc(CustomerDevelopment::getCreateTime);
        
        return this.list(queryWrapper);
    }
}