package com.bank.crm.service;

import com.bank.crm.entity.User;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 用户管理服务接口
 */
public interface UserService {

    /**
     * 获取用户列表
     */
    IPage<User> getUserList(int current, int size, String username, String role);

    /**
     * 根据ID获取用户
     */
    User getUserById(Integer id);

    /**
     * 添加用户
     */
    boolean addUser(User user);

    /**
     * 更新用户
     */
    boolean updateUser(User user);

    /**
     * 删除用户
     */
    boolean deleteUser(Integer id);

    /**
     * 重置密码
     */
    boolean resetPassword(Integer id);

    /**
     * 更新用户状态
     */
    boolean updateUserStatus(Integer id, boolean enabled);

    /**
     * 获取所有角色
     */
    List<String> getAllRoles();

    /**
     * 根据用户名获取用户
     */
    User getUserByUsername(String username);

    /**
     * 修改密码
     */
    boolean changePassword(Integer id, String oldPassword, String newPassword);
}