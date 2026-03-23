package com.bank.crm.service;

import com.bank.crm.entity.CustomerDevelopment;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 客户开发服务接口
 */
public interface DevelopmentService {
    
    /**
     * 获取客户开发列表
     */
    IPage<CustomerDevelopment> getDevelopmentList(Integer current, Integer size, String customerName, String progress, String status);
    
    /**
     * 根据ID获取客户开发
     */
    CustomerDevelopment getDevelopmentById(Integer id);
    
    /**
     * 保存客户开发
     */
    CustomerDevelopment saveDevelopment(CustomerDevelopment development);
    
    /**
     * 更新客户开发
     */
    CustomerDevelopment updateDevelopment(CustomerDevelopment development);
    
    /**
     * 删除客户开发
     */
    void deleteDevelopment(Integer id);
    
    /**
     * 获取客户开发总数
     */
    long count();
    
    /**
     * 获取指定时间前的客户开发数量
     */
    long countByCreateTimeBefore(java.time.LocalDate date);
    
    /**
     * 根据客户ID获取客户开发
     */
    List<CustomerDevelopment> getDevelopmentByCustomerId(Integer customerId);
}