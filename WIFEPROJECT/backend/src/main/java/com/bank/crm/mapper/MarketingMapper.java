package com.bank.crm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.crm.entity.MarketingOpportunity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 营销机会Mapper接口
 */
@Mapper
public interface MarketingMapper extends BaseMapper<MarketingOpportunity> {
}