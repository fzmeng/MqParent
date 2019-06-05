package com.study.mq.route;

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
public class RouteConsumer {

    private static final String QUEUE_NAME = "MQ.queue.route.one";

    @RabbitListener(queues = QUEUE_NAME)
    public void consume(Message message) {
        try {
            Thread.sleep(10L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.warn("Route-Model Consumers1 receive information: {}", new String(message.getBody()));
    }
}
