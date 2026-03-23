package com.bank.crm.service;

import com.bank.crm.entity.CustomerInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface CustomerService {

    IPage<CustomerInfo> getCustomerList(Integer current, Integer size, String name, String type, String level, String manager);

    CustomerInfo getCustomerById(Integer id);

    CustomerInfo saveCustomer(CustomerInfo customer);

    CustomerInfo updateCustomer(CustomerInfo customer);

    void deleteCustomer(Integer id);

    long count();

    long countByCreateTimeBefore(java.time.LocalDate date);
}