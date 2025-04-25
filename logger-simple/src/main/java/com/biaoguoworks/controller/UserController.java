package com.biaoguoworks.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.biaoguoworks.domain.User;
import com.biaoguoworks.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuxin
 * @date 2025/04/24 23:08:05
 */
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/get/user")
    public User selectUserById(@RequestParam("id") Long id) {
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getId, id));
    }
}
