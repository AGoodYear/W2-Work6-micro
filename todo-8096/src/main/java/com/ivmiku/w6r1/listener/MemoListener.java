package com.ivmiku.w6r1.listener;

import com.alibaba.fastjson2.JSON;
import com.ivmiku.w6r1.entity.Todo;
import com.ivmiku.w6r1.mapper.TodoMapper;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;

@Component
public class MemoListener {
    @Resource
    private TodoMapper todoMapper;

    @RabbitHandler
    @RabbitListener(queuesToDeclare = @Queue("memo_queue"))
    public void sendMsg(String message) {
        Todo todo = JSON.parseObject(message, Todo.class);
        todoMapper.insert(todo);
    }
}
