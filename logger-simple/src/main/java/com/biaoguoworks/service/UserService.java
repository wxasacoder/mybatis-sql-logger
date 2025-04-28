package com.biaoguoworks.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.biaoguoworks.domain.User;


/**
 *  Auto created by codeAppend plugin
 */
public interface UserService extends IService<User> {

    Page<User> pageListAll(long current, long size);

}