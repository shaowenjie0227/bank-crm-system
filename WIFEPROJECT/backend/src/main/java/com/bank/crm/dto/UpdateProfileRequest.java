package com.bank.crm.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 更新个人信息请求
 */
public class UpdateProfileRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50位之间")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}