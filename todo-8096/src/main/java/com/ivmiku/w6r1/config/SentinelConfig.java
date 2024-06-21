package com.ivmiku.w6r1.config;


import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.ivmiku.w6r1.response.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;

/**
 * 秒杀限流异常处理程序
 *
 * @author Miozus
 * @date 2022/03/30
 */
@Configuration
public class SentinelConfig implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(Result.error("系统繁忙，请稍后再试")));
    }
}
