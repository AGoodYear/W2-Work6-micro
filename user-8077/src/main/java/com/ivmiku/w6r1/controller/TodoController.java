package com.ivmiku.w6r1.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson2.JSON;
import com.ivmiku.w6r1.api.TodoService;
import com.ivmiku.w6r1.entity.ChangeQuery;
import com.ivmiku.w6r1.entity.SentinelHandler;
import com.ivmiku.w6r1.entity.Todo;
import com.ivmiku.w6r1.response.Result;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
@SaCheckLogin
public class TodoController {
    @DubboReference
    private TodoService todoService;

    @SentinelResource(value = "addTodo", blockHandler = "flowLimitHandler", blockHandlerClass = SentinelHandler.class)
    @PostMapping("/add")
    public Object addOne(@RequestBody Todo todo) {
        todo.setUserId(StpUtil.getLoginId().toString());
        todoService.insertOne(todo, StpUtil.getLoginId().toString());
        return JSON.toJSON(Result.ok());
    }

    @SentinelResource(value = "changeToPending", blockHandler = "flowLimitHandler", blockHandlerClass = SentinelHandler.class)
    @PostMapping("/pending")
    public Object changeToPending(@RequestBody ChangeQuery changeQuery) {
        changeQuery.setUserId(StpUtil.getLoginId().toString());
        int result;
        if (!changeQuery.getMemoId().isEmpty()) {
            result = todoService.changeToPengding(changeQuery.getMemoId(), StpUtil.getLoginId().toString());
        } else {
            result = todoService.changeAllToPending(changeQuery.getUserId());
        }
        if (result>0) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    @SentinelResource(value = "changeToFinish", blockHandler = "flowLimitHandler", blockHandlerClass = SentinelHandler.class)
    @PostMapping("/finish")
    public Object changeToFinish(@RequestBody ChangeQuery changeQuery) {
        changeQuery.setUserId(StpUtil.getLoginId().toString());
        int result;
        if (!changeQuery.getMemoId().isEmpty()) {
            result = todoService.changeToFinish(changeQuery.getMemoId(), StpUtil.getLoginId().toString());
        } else {
            result = todoService.changeAllToFinish(changeQuery.getUserId());
        }
        if (result>0) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    @SentinelResource(value = "search", blockHandler = "flowLimitHandler", blockHandlerClass = SentinelHandler.class)
    @GetMapping("/search")
    public Object searchMemo(@RequestParam String keyword, @RequestParam String flag, @RequestParam int page, @RequestParam int size) {
        return JSON.toJSON(Result.ok(todoService.search(keyword, flag, page, size, StpUtil.getLoginId().toString())));
    }

    @SentinelResource(value = "delete", blockHandler = "flowLimitHandler", blockHandlerClass = SentinelHandler.class)
    @GetMapping("delete")
    public Object delete(@RequestParam String todoId) {
        todoService.deleteOne(todoId);
        return JSON.toJSON(Result.ok());
    }

    @SentinelResource(value = "clear", blockHandler = "flowLimitHandler", blockHandlerClass = SentinelHandler.class)
    @GetMapping("clear")
    public Object deleteAll(@RequestParam String flag) {
        todoService.deleteAll(flag, StpUtil.getLoginId().toString());
        return JSON.toJSON(Result.ok());
    }
}
