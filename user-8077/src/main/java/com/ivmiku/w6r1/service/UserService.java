package com.ivmiku.w6r1.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ivmiku.w6r1.entity.User;
import com.ivmiku.w6r1.mapper.UserMapper;
import com.ivmiku.w6r1.response.Result;
import com.ivmiku.w6r1.utils.PasswordUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    public User selectUserByName(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }

    public Result register(String username, String password) {
        if (password.isEmpty() || username.isEmpty()) {
            return Result.error("用户名或密码非法！");
        }
        User user = new User();
        user.setUsername(username);
        user.setSalt(PasswordUtil.getSalt(10));
        user.setPassword(PasswordUtil.encrypt(password, user.getSalt()));
        userMapper.insert(user);
        return Result.ok();
    }

    public Result login(String username, String password) {
        User user = selectUserByName(username);
        if (user.getPassword().equals(PasswordUtil.encrypt(password, user.getSalt()))) {
            StpUtil.login(user.getId());
            HashMap<String, Object> hashMap = new HashMap<>(1);
            hashMap.put("token", StpUtil.getTokenValue());
            return Result.ok(hashMap);
        }
        return Result.error("用户名或密码错误！");
    }
}
