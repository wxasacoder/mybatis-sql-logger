package com.biaoguoworks.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biaoguoworks.dao.UserDao;
import com.biaoguoworks.domain.User;
import com.biaoguoworks.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Auto created by codeAppend plugin
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    // use "baseMapper" to call jdbc
    // example: baseMapper.insert(entity);
    // example: baseMapper.selectByPage(params);
   
}