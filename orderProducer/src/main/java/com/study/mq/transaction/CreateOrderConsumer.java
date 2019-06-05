package com.study.mq.transaction;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Description: 补单消费者
 * @Author mengfanzhu
 * @Date 2019/6/4 15:06
 * @Version 1.0
 */
@Component
public class CreateOrderConsumer {

    /**
     * 补单队列
     */
    public static final String CREATE_QUEUE_NAME_TRANSACTION = "MQ.trans.order.create.queue";

    @RabbitListener(queues = CREATE_QUEUE_NAME_TRANSACTION)
    public void orderReceiver(Message message, @Headers Map<String, Object> headers, Channel channel) throws IOException {
        //等我处理完再发新消息给我
        channel.basicQos(1);

        System.out.println("补单-消费者；" + JSONObject.toJSONString(message));
        String msgId = message.getMessageProperties().getMessageId();
        System.out.println("补单-msgId:" + msgId);
        String msgBody = new String(message.getBody(), "UTF-8");
        JSONObject jsonObject = JSONObject.parseObject(msgBody);
        String orderId = jsonObject.getString("orderId");
        System.out.println("补单-message body is " + jsonObject.toJSONString());
        //TODO 判断订单是否已经存在，若存在丢掉消息，否则手动签收消息
        Boolean isExist = true;
        if (isExist) {
            //手动签收消息，通知MQ 删除此消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } else {
            // 异常处理-丢弃消息
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }
}
