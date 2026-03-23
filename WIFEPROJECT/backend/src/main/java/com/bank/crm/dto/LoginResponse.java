package com.bank.crm.dto;

import lombok.Data;

/**
 * 登录响应DTO
 */
@Data
public class LoginResponse {
    private String token;
    private String username;
    private String role;
    private Long expiresIn;
}