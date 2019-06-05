package com.study.mq.transaction;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Description: 分布式事务处理-生产者
 * 场景1.
 * serviceA 创建订单
 * serviceB 派单
 * @Author mengfanzhu
 * @Date 2019/6/4 14:19
 * @Version 1.0
 */
@Component
@Slf4j
public class OrderProducer implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 派单队列
     */
    public static final String QUEUE_NAME_TRANSACTION = "MQ.trans.order.queue";
    /**
     * 补单队列
     */
    public static final String CREATE_QUEUE_NAME_TRANSACTION = "MQ.trans.order.create.queue";
    /**
     * 派单路由KEY
     */
    public static final String ROUTE_NAME_TRANSACTION = "MQ.trans.order.route";
    /**
     * 派单交换机
     */
    public static final String EXCHANGE_NAME_TRANSACTION = "MQ.trans.order.exchange";

    public void saveOrder() {
        String orderId = UUID.randomUUID().toString();
        //insert into t_order_info
        //send mq 派单
        sendMsg(orderId);
    }

    /**
     * 派单
     *
     * @param orderId
     */
    public void sendMsg(String orderId) {
        //分布式事务需要
//        rabbitTemplate.setMandatory(true);
//        rabbitTemplate.setConfirmCallback(this);

        String msg = "Transactional message";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", msg);
        jsonObject.put("orderId", orderId);
        //消息封装
        Message message = MessageBuilder.withBody(jsonObject.toJSONString().getBytes()).setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding("utf-8").setMessageId(orderId).build();

        //回调返回的数据
        CorrelationData correlationData = new CorrelationData(orderId);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME_TRANSACTION, ROUTE_NAME_TRANSACTION, message, correlationData);
        System.out.println("Transactional Message success ,body is " + jsonObject.toJSONString());
        System.out.println("correlationData is " + JSONObject.toJSONString(correlationData));
    }

    /**
     * 派单队列
     *
     * @return
     */
    @Bean
    public Queue orderReceiver() {
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

    @Bean
    public Binding bindingExchangeOrderReceiverQueue() {
        return BindingBuilder.bind(orderReceiver()).to(transExchange()).with(ROUTE_NAME_TRANSACTION);
    }

    @Bean
    public Binding bindingExchangeCreateOrderQueue() {
        return BindingBuilder.bind(createOrderReceiver()).to(transExchange()).with(ROUTE_NAME_TRANSACTION);
    }

    /**
     * 用来确认消息是否有送达消息队列
     *
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println(JSONObject.toJSONString(correlationData));
        System.out.println(cause);
        if (ack) {
            System.out.println("success");
        } else {
            // retry
            System.out.println("failed");
        }
    }

    /**
     * 消息找不到对应的Exchange会先触发此方法
     *
     * @param message
     * @param replyCode
     * @param replyText
     * @param tempExchange
     * @param tmpRoutingKey
     */
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String tempExchange
            , String tmpRoutingKey) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Send message failed:" + replyCode + " " + replyText);
        rabbitTemplate.send(message);
    }
}
