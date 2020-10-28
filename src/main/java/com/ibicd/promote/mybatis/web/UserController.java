package com.ibicd.promote.mybatis.web;

import com.ibicd.promote.mybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/findById")
    public String findById(@RequestParam int id) {
        return userService.findById(id).getUserName();
    }
}
