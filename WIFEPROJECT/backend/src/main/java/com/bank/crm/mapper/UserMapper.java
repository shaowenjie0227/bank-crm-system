package com.bank.crm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bank.crm.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户信息表Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 获取所有角色
     */
    @Select("SELECT DISTINCT role FROM user WHERE role IS NOT NULL AND role != ''")
    List<String> selectAllRoles();
}