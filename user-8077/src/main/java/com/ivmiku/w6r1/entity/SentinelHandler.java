package com.ivmiku.w6r1.entity;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.fastjson2.JSON;
import com.ivmiku.w6r1.response.Result;

/**
 * @author Aurora
 */
public class SentinelHandler {
    public static Object flowLimitHandler(BlockExceptionHandler e) {
        return JSON.toJSON(Result.error("系统繁忙，请稍后再试！"));
    }
}
