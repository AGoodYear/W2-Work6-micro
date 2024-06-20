package com.ivmiku.w6r1;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.ivmiku.w6r1.mapper")
@EnableDubbo
@EnableAsync
public class Main8096 {
    public static void main(String[] args) {
        SpringApplication.run(Main8096.class, args);
    }
}