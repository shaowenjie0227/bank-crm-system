package com.bank.crm.mapper;

import com.bank.crm.entity.CustomerInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户信息 Mapper 接口
 */
@Mapper
public interface CustomerInfoMapper extends BaseMapper<CustomerInfo> {
}