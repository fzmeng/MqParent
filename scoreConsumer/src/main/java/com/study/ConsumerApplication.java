package com.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

/**
 * @Description:
 * @Author mengfanzhu
 * @Date 2019/6/4 22:11
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan("com.study.mapper")
public class ConsumerApplication {
    public static void main(String[] args) {
        System.out.println("SocreConsumer APP start...");
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
