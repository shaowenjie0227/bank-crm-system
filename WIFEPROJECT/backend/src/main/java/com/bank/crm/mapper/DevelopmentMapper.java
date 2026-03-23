package com.bank.crm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.crm.entity.CustomerDevelopment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户开发Mapper接口
 */
@Mapper
public interface DevelopmentMapper extends BaseMapper<CustomerDevelopment> {
}