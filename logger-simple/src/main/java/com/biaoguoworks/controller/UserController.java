package com.biaoguoworks.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biaoguoworks.domain.User;
import com.biaoguoworks.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @GetMapping("/get/user-all/page")
    public Page<User> getUserALlPage() {
        return userService.pageListAll(1,100);
    }

    @GetMapping("/get/user/update")
    public Boolean userUpdate() {
        return userService.update(Wrappers.<User>lambdaUpdate().set(User::getName, "wx1").eq(User::getId, 1));

    }
    @GetMapping("/get/user/insert")
    public Boolean userInsert() {
        List<User> collect = IntStream.range(1, 100).mapToObj(r -> {
            User user = new User();
            user.setName("我是用户" + r);
            return user;
        }).collect(Collectors.toList());
        return userService.saveBatch(collect);
    }
}
