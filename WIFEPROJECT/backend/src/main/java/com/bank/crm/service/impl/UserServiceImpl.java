package com.bank.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bank.crm.entity.User;
import com.bank.crm.mapper.UserMapper;
import com.bank.crm.service.UserService;
import com.bank.crm.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public UserMapper getBaseMapper() {
        return super.getBaseMapper();
    }

    @Autowired
    private UserMapper userMapper;

    @Override
    public IPage<User> getUserList(int current, int size, String username, String role) {
        Page<User> page = new Page<>(current, size);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        
        // 用户名筛选
        if (username != null && !username.trim().isEmpty()) {
            queryWrapper.like(User::getUsername, username.trim());
        }
        
        // 角色筛选
        if (role != null && !role.trim().isEmpty()) {
            queryWrapper.like(User::getRole, role.trim());
        }
        
        // 按创建时间降序排序
        queryWrapper.orderByDesc(User::getCreateTime);
        
        return this.page(page, queryWrapper);
    }

    @Override
    public User getUserById(Integer id) {
        return this.getById(id);
    }

    @Override
    @Transactional
    public boolean addUser(User user) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        if (this.count(queryWrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 设置默认密码（123456）
        String defaultPassword = "123456";
        String salt = MD5Util.generateSalt();
        String encryptedPassword = MD5Util.encryptPassword(defaultPassword, salt);
        
        user.setPassword(encryptedPassword);
        user.setSalt(salt);
        user.setStatus(1); // 默认启用
        
        return this.save(user);
    }

    @Override
    @Transactional
    public boolean updateUser(User user) {
        // 检查用户名是否与其他用户冲突
        if (user.getUsername() != null) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUsername, user.getUsername())
                         .ne(User::getId, user.getId());
            if (this.count(queryWrapper) > 0) {
                throw new RuntimeException("用户名已存在");
            }
        }
        
        // 不更新密码字段
        user.setPassword(null);
        user.setSalt(null);
        
        return this.updateById(user);
    }

    @Override
    @Transactional
    public boolean deleteUser(Integer id) {
        // 不能删除自己
        User currentUser = getCurrentUser();
        if (currentUser.getId().equals(id)) {
            throw new RuntimeException("不能删除当前登录用户");
        }
        
        return this.removeById(id);
    }

    @Override
    @Transactional
    public boolean resetPassword(Integer id) {
        User user = this.getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        String defaultPassword = "123456";
        String salt = MD5Util.generateSalt();
        String encryptedPassword = MD5Util.encryptPassword(defaultPassword, salt);
        
        user.setPassword(encryptedPassword);
        user.setSalt(salt);
        
        return this.updateById(user);
    }

    @Override
    @Transactional
    public boolean updateUserStatus(Integer id, boolean enabled) {
        User user = this.getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 不能禁用自己
        User currentUser = getCurrentUser();
        if (currentUser.getId().equals(id) && !enabled) {
            throw new RuntimeException("不能禁用当前登录用户");
        }
        
        user.setStatus(enabled ? 1 : 0);
        return this.updateById(user);
    }

    @Override
    public List<String> getAllRoles() {
        return userMapper.selectAllRoles();
    }

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return this.getOne(queryWrapper);
    }

    @Override
    @Transactional
    public boolean changePassword(Integer id, String oldPassword, String newPassword) {
        User user = this.getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证旧密码
        String encryptedOldPassword = MD5Util.encryptPassword(oldPassword, user.getSalt());
        if (!encryptedOldPassword.equals(user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }
        
        // 设置新密码
        String salt = MD5Util.generateSalt();
        String encryptedNewPassword = MD5Util.encryptPassword(newPassword, salt);
        
        user.setPassword(encryptedNewPassword);
        user.setSalt(salt);
        
        return this.updateById(user);
    }
    
    /**
     * 获取当前登录用户
     */
    private User getCurrentUser() {
        // 从Spring Security上下文中获取当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUsername, authentication.getName());
            return this.getOne(queryWrapper);
        }
        throw new RuntimeException("未找到当前登录用户");
    }
}