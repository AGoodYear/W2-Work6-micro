package com.ivmiku.w6r1.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.ivmiku.w6r1.api.TodoService;
import com.ivmiku.w6r1.entity.ChangeQuery;
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

    @PostMapping("/add")
    public Object addOne(@RequestBody Todo todo) {
        todo.setUserId(StpUtil.getLoginId().toString());
        todoService.insertOne(todo, StpUtil.getLoginId().toString());
        return JSON.toJSON(Result.ok());
    }

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

    @GetMapping("/search")
    public Object searchMemo(@RequestParam String keyword, @RequestParam String flag, @RequestParam int page, @RequestParam int size) {
        return JSON.toJSON(Result.ok(todoService.search(keyword, flag, page, size, StpUtil.getLoginId().toString())));
    }

    @GetMapping("delete")
    public Object delete(@RequestParam String todoId) {
        todoService.deleteOne(todoId);
        return JSON.toJSON(Result.ok());
    }

    @GetMapping("clear")
    public Object deleteAll(@RequestParam String flag) {
        todoService.deleteAll(flag, StpUtil.getLoginId().toString());
        return JSON.toJSON(Result.ok());
    }

    @GetMapping
    public Object getTodo(@RequestParam String flag, @RequestParam int page, @RequestParam int size) {
        return JSON.toJSON(Result.ok(todoService.getAllTodo(StpUtil.getLoginId().toString(), page, size, flag)));
    }
}
