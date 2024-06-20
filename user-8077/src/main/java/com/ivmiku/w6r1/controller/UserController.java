package com.ivmiku.w6r1.controller;

import com.alibaba.fastjson2.JSON;
import com.ivmiku.w6r1.entity.UserInput;
import com.ivmiku.w6r1.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Object login(@RequestBody UserInput input) {
        return JSON.toJSON(userService.login(input.getUsername(), input.getPassword()));
    }

    @PostMapping("/register")
    public Object register(@RequestBody UserInput input) {
        return JSON.toJSON(userService.register(input.getUsername(), input.getPassword()));
    }
}
