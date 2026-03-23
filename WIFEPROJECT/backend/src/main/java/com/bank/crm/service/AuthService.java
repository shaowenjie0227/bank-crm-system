package com.bank.crm.service;

import com.bank.crm.dto.LoginRequest;
import com.bank.crm.dto.LoginResponse;
import com.bank.crm.entity.User;

/**
 * 认证服务接口
 */
public interface AuthService {
    
    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest loginRequest);
    
    /**
     * 获取当前用户信息
     */
    User getCurrentUser();
}