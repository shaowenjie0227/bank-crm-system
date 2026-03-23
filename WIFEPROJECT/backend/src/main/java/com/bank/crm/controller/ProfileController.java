package com.bank.crm.controller;

import com.bank.crm.dto.UpdatePasswordRequest;
import com.bank.crm.dto.UpdateProfileRequest;
import com.bank.crm.entity.User;
import com.bank.crm.service.AuthService;
import com.bank.crm.service.UserService;
import com.bank.crm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 个人中心控制器
 */
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    /**
     * 获取个人信息
     */
    @GetMapping
    public Result<User> getProfile() {
        User currentUser = authService.getCurrentUser();
        return Result.success(currentUser);
    }

    /**
     * 更新个人信息
     */
    @PutMapping
    public Result<String> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        User currentUser = authService.getCurrentUser();
        
        // 只允许更新部分字段
        currentUser.setUsername(request.getUsername());
        
        boolean result = userService.updateUser(currentUser);
        return result ? Result.success("个人信息更新成功") : Result.error("个人信息更新失败");
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<String> changePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        User currentUser = authService.getCurrentUser();
        
        boolean result = userService.changePassword(
            currentUser.getId(),
            request.getOldPassword(),
            request.getNewPassword()
        );
        
        return result ? Result.success("密码修改成功") : Result.error("密码修改失败");
    }

    /**
     * 获取当前用户的权限信息
     */
    @GetMapping("/permissions")
    public Result<UserPermissionsResponse> getPermissions() {
        User currentUser = authService.getCurrentUser();
        
        UserPermissionsResponse response = new UserPermissionsResponse();
        response.setUsername(currentUser.getUsername());
        response.setRole(currentUser.getRole());
        response.setPermissions(getRolePermissions(currentUser.getRole()));
        
        return Result.success(response);
    }

    /**
     * 根据角色获取权限列表
     */
    private java.util.List<String> getRolePermissions(String role) {
        java.util.List<String> permissions = new java.util.ArrayList<>();
        
        switch (role) {
            case "管理员":
                permissions.add("user:manage");
                permissions.add("role:manage");
                permissions.add("customer:view");
                permissions.add("customer:edit");
                permissions.add("customer:delete");
                permissions.add("marketing:view");
                permissions.add("marketing:edit");
                permissions.add("marketing:delete");
                permissions.add("development:view");
                permissions.add("development:edit");
                permissions.add("development:delete");
                permissions.add("profile:view");
                permissions.add("profile:edit");
                break;
            case "销售":
                permissions.add("customer:view");
                permissions.add("customer:edit");
                permissions.add("marketing:view");
                permissions.add("marketing:edit");
                permissions.add("development:view");
                permissions.add("development:edit");
                permissions.add("profile:view");
                permissions.add("profile:edit");
                break;
            case "客服":
                permissions.add("customer:view");
                permissions.add("customer:edit");
                permissions.add("profile:view");
                permissions.add("profile:edit");
                break;
            default:
                permissions.add("profile:view");
                permissions.add("profile:edit");
                break;
        }
        
        return permissions;
    }

    /**
     * 用户权限响应类
     */
    public static class UserPermissionsResponse {
        private String username;
        private String role;
        private java.util.List<String> permissions;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public java.util.List<String> getPermissions() {
            return permissions;
        }

        public void setPermissions(java.util.List<String> permissions) {
            this.permissions = permissions;
        }
    }
}