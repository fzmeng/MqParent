package com.study.mq.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Description: Hello 模式
 * @Author mengfanzhu
 * @Date 2019/5/31 13:51
 * @Version 1.0
 */
@Component
@Slf4j
public class HelloProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 定义一个队列名称
     */
    private static final String QUEUE_NAME = "MQ.hello";

    /**
     * 创建QUEUE
     * @return
     */
    @Bean
    public Queue queue(){
        return new Queue(QUEUE_NAME,true);
    }

    public void send() {
        String message = "Hello mq";
        amqpTemplate.convertAndSend(QUEUE_NAME, message);
        log.warn("Hello-Model send success");
    }

}
