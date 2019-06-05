package com.study.mqconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 订单消息MQconfig
 * @Author mengfanzhu
 * @Date 2019/6/4 22:28
 * @Version 1.0
 */
@Configuration
@Slf4j
public class OrderMQMsgConfig {

    /**
     * 积分处理队列
     */
    public static final String QUEUE_NAME_TRANSACTION = "api.score.queue";
    /**
     * 补单队列
     */
    public static final String CREATE_QUEUE_NAME_TRANSACTION = "api.order.reCreate.queue";
    /**
     * 积分处理路由KEY
     */
    public static final String ROUTE_NAME_TRANSACTION = "api.score.route";
    /**
     * 积分处理交换机
     */
    public static final String EXCHANGE_NAME_TRANSACTION = "api.score.exchange";

    /**
     * 积分队列
     *
     * @return
     */
    @Bean
    public Queue scoreQueue() {
        return new Queue(QUEUE_NAME_TRANSACTION, true);
    }

    /**
     * 补单队列
     *
     * @return
     */
    @Bean
    public Queue createOrderReceiver() {
        return new Queue(CREATE_QUEUE_NAME_TRANSACTION, true);
    }

    @Bean
    public DirectExchange transExchange() {
        return new DirectExchange(EXCHANGE_NAME_TRANSACTION);
    }

    /**
     * 交换机绑定到积分队列
     * @return
     */
    @Bean
    public Binding bindingExchangeOrderReceiverQueue() {
        return BindingBuilder.bind(scoreQueue()).to(transExchange()).with(ROUTE_NAME_TRANSACTION);
    }

    /**
     * 交换机绑定到补单队列
     * @return
     */
    @Bean
    public Binding bindingExchangeCreateOrderQueue() {
        return BindingBuilder.bind(createOrderReceiver()).to(transExchange()).with(ROUTE_NAME_TRANSACTION);
    }
}
