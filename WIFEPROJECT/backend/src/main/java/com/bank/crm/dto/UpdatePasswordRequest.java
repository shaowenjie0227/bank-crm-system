package com.bank.crm.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 修改密码请求
 */
public class UpdatePasswordRequest {

    @NotBlank(message = "旧密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}