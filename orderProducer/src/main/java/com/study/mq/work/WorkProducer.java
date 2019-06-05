package com.study.mq.work;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Description: work queue
 * @Author mengfanzhu
 * @Date 2019/5/31 14:32
 * @Version 1.0
 */
@Component
@Slf4j
public class WorkProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 定义一个队列名称
     */
    private static final String QUEUE_NAME = "MQ.work";

    /**
     * 创建QUEUE
     *
     * @return
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }

    public void send() {
        for (int i = 0; i < 500; i++) {
            String message = "work mq " + i;
            amqpTemplate.convertAndSend(QUEUE_NAME, message);
        }
        log.warn("Work-Model send success");
    }
}
