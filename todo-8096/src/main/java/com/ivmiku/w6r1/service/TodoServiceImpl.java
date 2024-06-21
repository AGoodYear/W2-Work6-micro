package com.ivmiku.w6r1.service;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ivmiku.w6r1.api.TodoService;
import com.ivmiku.w6r1.entity.Todo;
import com.ivmiku.w6r1.mapper.TodoMapper;
import com.ivmiku.w6r1.utils.RedisUtil;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@DubboService
public class TodoServiceImpl implements TodoService {
    @Resource
    private TodoMapper todoMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void insertOne(Todo todo, String userId) {
        rabbitTemplate.convertAndSend("memo_queue", JSON.toJSONString(todo));
        String key = "memo:" + userId;
        if (redisUtil.ifExist(key)) {
            redisUtil.listAdd(key, todo);
            redisUtil.setExpireTime(key);
        } else {
            loadRedis(userId);
            redisUtil.listAdd(key, todo);
        }
    }

    public void loadRedis(String userId) {
        String key = "memo" + userId;
        List<Todo> list = getAllTodo(userId, 1, 10, "all");
        for (Todo todo1 : list) {
            redisUtil.listAdd(key, todo1);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int changeToPengding(String todoId, String userId) {
        Todo todo = todoMapper.selectById(todoId);
        UpdateWrapper<Todo> updateWrapper = Wrappers.update(todo);
        updateWrapper.set("status", "pending");
        redisUtil.deleteKey("memo:" + userId);
        return todoMapper.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeToFinish(String todoId, String userId) {
        Todo todo = todoMapper.selectById(todoId);
        UpdateWrapper<Todo> updateWrapper = Wrappers.update(todo);
        updateWrapper.set("status", "finish");
        redisUtil.deleteKey("memo:" + userId);
        return todoMapper.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeAllToPending(String userId) {
        UpdateWrapper<Todo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId).eq("status", "finish");
        updateWrapper.set("status", "pending");
        redisUtil.deleteKey("memo:" + userId);
        return todoMapper.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeAllToFinish(String userId) {
        UpdateWrapper<Todo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId).eq("status", "pending");
        updateWrapper.set("status", "finish");
        redisUtil.deleteKey("memo:" + userId);
        return todoMapper.update(updateWrapper);
    }

    @Override
    public List<Todo> getAllTodo(String userId, int current, int size, String flag) {
        QueryWrapper<Todo> queryWrapper = new QueryWrapper<>();
        Page<Todo> page = new Page<>(current, size);
        queryWrapper.eq("user_id", userId).orderByDesc("created_at");
        if (!"all".equals(flag)) {
            queryWrapper.eq("status", flag);
        }
        return todoMapper.selectPage(page, queryWrapper).getRecords();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteOne(String todoId) {
        QueryWrapper<Todo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", todoId);
        redisUtil.deleteKey("memo:" + StpUtil.getLoginId());
        return todoMapper.delete(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAll(String flag, String userId) {
        QueryWrapper<Todo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        if ("finish".equals(flag)) {
            queryWrapper.eq("status", "finish");
        } else if ("pending".equals(flag)) {
            queryWrapper.eq("status", "pending");
        }
        redisUtil.deleteKey("memo:" + userId);
        return todoMapper.delete(queryWrapper);
    }

    @Override
    public List<Todo> search(String keyword, String flag, int page, int size, String userId) {
        List<Todo> result = new ArrayList<>();
        int start = page * size - size;
        int end = page * size - 1;
        String key = "memo:" + userId;
        if ("all".equals(flag)) {
            result.addAll(redisUtil.listGet(key, start, size));
            if (result.isEmpty()) {
                loadRedis(userId);
            }
            if (result.size() == (end - start + 1)) {
                return result;
            }
            int redisSize = result.size();
            QueryWrapper<Todo> queryWrapper = new QueryWrapper<>();
            Page<Todo> pager = new Page<>(((end - result.size()) / size) + 1, size);
            queryWrapper.eq("user_id", userId).like("content", keyword).orderByDesc("created_at");
            result.addAll(todoMapper.selectPage(pager, queryWrapper).getRecords().subList(redisSize, -1));
        } else {
            QueryWrapper<Todo> queryWrapper = new QueryWrapper<>();
            Page<Todo> pager = new Page<>(((end) / size) + 1, size);
            queryWrapper.eq("user_id", userId).like("content", keyword).eq("status", flag).orderByDesc("created_at");
            result.addAll(todoMapper.selectPage(pager, queryWrapper).getRecords());
        }
        return result;
    }
}
