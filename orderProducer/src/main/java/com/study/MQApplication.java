package com.study;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description:
 * @Author mengfanzhu
 * @Date 2019/5/31 14:12
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan("com.study.mapper")
public class MQApplication {

    public static void main(String[] args) {
        System.out.println("APP start...");
        SpringApplication.run(MQApplication.class, args);
    }
}
