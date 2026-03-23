package com.bank.crm.controller;

import com.bank.crm.entity.User;
import com.bank.crm.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 临时密码更新控制器
 */
@RestController
@RequestMapping("/api/temp")
public class TempPasswordController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam String username, @RequestParam String newPassword) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        
        if (user != null) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userMapper.updateById(user);
            return "密码更新成功: " + encodedPassword;
        } else {
            return "用户不存在";
        }
    }
}