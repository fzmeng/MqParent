package com.study.mq.publish;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Description: 发布/订阅
 * @Author mengfanzhu
 * @Date 2019/5/31 15:15
 * @Version 1.0
 */
@Component
@Slf4j
public class PublishProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send() {
        for (int i = 0; i < 10; i++) {
            String message = "publish mq " + i;
            //第一个参数为exchangeName,第二个是router name 此时用不了，第三个是发送内容
            amqpTemplate.convertAndSend(EXCHANGE_NAME,"", message);
        }
        log.warn("Publish-Model send success");
    }

    private static final String QUEUE_NAME = "MQ.publish.one";
    private static final String QUEUE_NAME_2 = "MQ.publish.two";
    private static final String EXCHANGE_NAME = "MQ.ex.publish";

    /**
     * 创建QUEUE
     * @return
     */
    @Bean
    public Queue queueOne() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Queue queueTwo() {
        return new Queue(QUEUE_NAME_2, true);
    }

    /**
     * 创建publish exchange
     * fanout
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_NAME);
    }

    /**
     * queue 与 交换机绑定
     * @return
     */
    @Bean
    public Binding bindingExchangeWithQueue() {
        return BindingBuilder.bind(queueOne()).to(fanoutExchange());
    }

    /**
     *  queue2 与 交换机绑定
     * @return
     */
    @Bean
    public Binding bindingExchangeWithQueue2() {
        return BindingBuilder.bind(queueTwo()).to(fanoutExchange());
    }

}
