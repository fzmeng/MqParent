package com.study.mq.work;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Description: work 消费者
 * @Author mengfanzhu
 * @Date 2019/5/31 13:55
 * @Version 1.0
 */
@Component
@Slf4j
public class WorkConsumer {


    private static final String QUEUE_NAME = "MQ.work";//定义一个队列名称

    @RabbitListener(queues = QUEUE_NAME)
    public void consume(Message message) {
        try {
            Thread.sleep(10L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.warn("Work-Model Consumers1 receive information: {}", new String(message.getBody()));
    }
}
