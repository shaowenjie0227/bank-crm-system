package com.bank.crm.controller;

import com.bank.crm.dto.LoginRequest;
import com.bank.crm.dto.LoginResponse;
import com.bank.crm.entity.User;
import com.bank.crm.service.AuthService;
import com.bank.crm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return Result.success(response);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        // 在实际应用中，可以将token加入黑名单
        return Result.success("登出成功");
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<User> getCurrentUser() {
        User user = authService.getCurrentUser();
        return Result.success(user);
    }
}