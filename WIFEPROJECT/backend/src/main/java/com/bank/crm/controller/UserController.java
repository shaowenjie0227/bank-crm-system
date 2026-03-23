package com.bank.crm.controller;

import com.bank.crm.entity.User;
import com.bank.crm.service.UserService;
import com.bank.crm.vo.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasRole('管理员')")
    public Result<IPage<User>> getUserList(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String role) {
        
        IPage<User> page = userService.getUserList(current, size, username, role);
        return Result.success(page);
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('管理员')")
    public Result<User> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return Result.success(user);
    }

    /**
     * 添加用户
     */
    @PostMapping
    @PreAuthorize("hasRole('管理员')")
    public Result<String> addUser(@Valid @RequestBody User user) {
        boolean result = userService.addUser(user);
        return result ? Result.success("用户添加成功") : Result.error("用户添加失败");
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('管理员')")
    public Result<String> updateUser(@PathVariable Integer id, @Valid @RequestBody User user) {
        user.setId(id);
        boolean result = userService.updateUser(user);
        return result ? Result.success("用户更新成功") : Result.error("用户更新失败");
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('管理员')")
    public Result<String> deleteUser(@PathVariable Integer id) {
        boolean result = userService.deleteUser(id);
        return result ? Result.success("用户删除成功") : Result.error("用户删除失败");
    }

    /**
     * 重置密码
     */
    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('管理员')")
    public Result<String> resetPassword(@PathVariable Integer id) {
        boolean result = userService.resetPassword(id);
        return result ? Result.success("密码重置成功") : Result.error("密码重置失败");
    }

    /**
     * 启用/禁用用户
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('管理员')")
    public Result<String> updateUserStatus(@PathVariable Integer id, @RequestParam boolean enabled) {
        boolean result = userService.updateUserStatus(id, enabled);
        return result ? Result.success("状态更新成功") : Result.error("状态更新失败");
    }

    /**
     * 获取所有角色
     */
    @GetMapping("/roles")
    @PreAuthorize("hasRole('管理员')")
    public Result<List<String>> getAllRoles() {
        List<String> roles = userService.getAllRoles();
        return Result.success(roles);
    }
}