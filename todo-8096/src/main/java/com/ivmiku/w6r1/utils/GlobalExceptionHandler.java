package com.ivmiku.w6r1.utils;

import cn.dev33.satoken.exception.SaTokenException;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson2.JSON;
import com.ivmiku.w6r1.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;
import java.text.ParseException;

/**
 * @author Aurora
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = SaTokenException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object SaTokenExceptionHandler(SaTokenException e) {
        log.error("SaTokenException:" + e.getLocalizedMessage());
        return JSON.toJSON(Result.error("身份认证错误：" + e.getMessage()));
    }

    @ExceptionHandler(value = ParseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object parseExceptionHandler(ParseException e) {

        log.error("ParseException:" + e.getLocalizedMessage());
        return JSON.toJSON(Result.error("请检查日期是否合法"));
    }

    @ExceptionHandler(value = SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object SQLExceptionHandler(SQLException e) {
        log.error("SQLException:" + e.getLocalizedMessage());
        return JSON.toJSON(Result.error("数据库出错！（内部错误）\n" + e.getMessage()));
    }

    @ExceptionHandler(value = BlockException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object sentinelHandler(BlockException e) {
        return JSON.toJSON(Result.error("系统繁忙，请稍后再试"));
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object exceptionHandler(Exception e) {
        log.error("Exception:" + e.getLocalizedMessage());
        return JSON.toJSON(Result.error("服务器内部错误:" + e.getClass() + ":" +  e.getMessage()));
    }
}
