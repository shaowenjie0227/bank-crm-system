package com.bank.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bank.crm.entity.CustomerInfo;
import com.bank.crm.mapper.CustomerInfoMapper;
import com.bank.crm.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerInfoMapper, CustomerInfo> implements CustomerService {

    @Override
    public CustomerInfoMapper getBaseMapper() {
        return super.getBaseMapper();
    }

    @Override
    public IPage<CustomerInfo> getCustomerList(Integer current, Integer size, String name, String type, String level, String manager) {
        Page<CustomerInfo> page = new Page<>(current, size);
        LambdaQueryWrapper<CustomerInfo> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(name)) {
            queryWrapper.like(CustomerInfo::getCustomerName, name);
        }
        if (StringUtils.hasText(type)) {
            queryWrapper.eq(CustomerInfo::getCustomerType, type);
        }
        if (StringUtils.hasText(level)) {
            queryWrapper.eq(CustomerInfo::getCustomerLevel, level);
        }
        if (StringUtils.hasText(manager)) {
            queryWrapper.eq(CustomerInfo::getCustomerManager, manager);
        }

        queryWrapper.orderByDesc(CustomerInfo::getCreateTime);
        return this.page(page, queryWrapper);
    }

    @Override
    public CustomerInfo getCustomerById(Integer id) {
        return this.getById(id);
    }

    @Override
    public CustomerInfo saveCustomer(CustomerInfo customer) {
        customer.setId(null);
        boolean result = this.save(customer);
        if (!result) {
            throw new RuntimeException("保存客户失败");
        }
        return customer;
    }

    @Override
    public CustomerInfo updateCustomer(CustomerInfo customer) {
        boolean result = this.updateById(customer);
        if (!result) {
            throw new RuntimeException("更新客户失败");
        }
        return customer;
    }

    @Override
    public void deleteCustomer(Integer id) {
        this.removeById(id);
    }

    @Override
    public long count() {
        return super.count();
    }

    @Override
    public long countByCreateTimeBefore(java.time.LocalDate date) {
        LambdaQueryWrapper<CustomerInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.lt(CustomerInfo::getCreateTime, date.atStartOfDay());
        return this.count(queryWrapper);
    }
}