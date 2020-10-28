package com.ibicd.promote.mybatis.service;

import com.ibicd.promote.mybatis.domain.User;
import com.ibicd.promote.mybatis.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User findById(int id) {
        return userMapper.findById(id);
    }

    @Autowired
    private UserMapper userMapper;

}
