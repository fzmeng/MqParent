package com.study.mq.topic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Description: topic 模式
 * @Author mengfanzhu
 * @Date 2019/5/31 15:15
 * @Version 1.0
 */
@Component
@Slf4j
public class TopicProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;


    /**
     * 多个词匹配
     */
    public void sendTopicN() {
        for (int i = 0; i < 20; i++) {
            String message = "Topic N mq " + i;
            //第一个参数为exchangeName,第二个是topic key，第三个是发送内容
            amqpTemplate.convertAndSend(EXCHANGE_NAME, "top2.queue.msg", message);
        }
        log.warn("Topic-Model send success");
    }

    /**
     * 一个词匹配
     */
    public void sendTopic1() {
        for (int i = 20; i < 50; i++) {
            String message = "Topic1 mq " + i;
            //第一个参数为exchangeName,第二个是topic key，第三个是发送内容
            amqpTemplate.convertAndSend(EXCHANGE_NAME, "top2.queue", message);
        }
        log.warn("Route-Model send success");
    }


    private static final String QUEUE_NAME = "MQ.queue.topic.one";
    private static final String QUEUE_NAME_2 = "MQ.queue.topic.two";
    private static final String EXCHANGE_NAME = "MQ.topic";

    /**
     * 创建QUEUE
     *
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
     * topic exchange
     *
     * @return
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    /**
     * queue 与 交换机绑定  指定路由KEY1
     * 星号 表示 one word
     * @return
     */
    @Bean
    public Binding bindingTopicExchangeWithQueue() {
        return BindingBuilder.bind(queueOne()).to(topicExchange()).with("top2.#");
    }

    /**
     * queue2 与 交换机绑定 指定路由KEY2
     * # 号 表示 more word
     * @return
     */
    @Bean
    public Binding bindingTopicExchangeWithQueue2() {
        return BindingBuilder.bind(queueTwo()).to(topicExchange()).with("top2.queue.#");
    }

}
