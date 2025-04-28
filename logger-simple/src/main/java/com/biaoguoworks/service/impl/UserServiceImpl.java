package com.biaoguoworks.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biaoguoworks.dao.UserDao;
import com.biaoguoworks.domain.User;
import com.biaoguoworks.service.UserService;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;

/**
 * Auto created by codeAppend plugin
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Override
    public Page<User> pageListAll(long current, long size) {
        return page(new Page<>(current, size), Wrappers.<User>lambdaQuery().le(User::getId, 1000));
    }

}