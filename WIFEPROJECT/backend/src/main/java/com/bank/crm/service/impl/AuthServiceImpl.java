package com.bank.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bank.crm.dto.LoginRequest;
import com.bank.crm.dto.LoginResponse;
import com.bank.crm.entity.User;
import com.bank.crm.mapper.UserMapper;
import com.bank.crm.service.AuthService;
import com.bank.crm.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 进行身份验证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 获取用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Qydh_Name", loginRequest.getUsername());
        User user = userMapper.selectOne(queryWrapper);

        // 生成JWT令牌
        String token = jwtUtil.generateToken(loginRequest.getUsername(), user.getRole());

        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsername(loginRequest.getUsername());
        response.setRole(user.getRole());
        response.setExpiresIn(expiration);

        return response;
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Qydh_Name", username);
        return userMapper.selectOne(queryWrapper);
    }
}