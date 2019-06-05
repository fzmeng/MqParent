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
 * @Description: 派单消费者
 * @Author mengfanzhu
 * @Date 2019/6/4 15:06
 * @Version 1.0
 */
@Component
public class OrderReceiverConsumer {

    /**
     * 派单队列
     */
    public static final String QUEUE_NAME_TRANSACTION = "MQ.trans.order.queue";

    @RabbitListener(queues = QUEUE_NAME_TRANSACTION)
    public void orderReceiver(Message message, @Headers Map<String, Object> headers, Channel channel) throws IOException {
        //等我处理完再发新消息给我
        channel.basicQos(1);

        System.out.println("派单消费者；" + JSONObject.toJSONString(message));
        String msgId = message.getMessageProperties().getMessageId();
        System.out.println("派单-msgId:" + msgId);
        String msgBody = new String(message.getBody(), "UTF-8");
        JSONObject jsonObject = JSONObject.parseObject(msgBody);
        String orderId = jsonObject.getString("orderId");
        System.out.println("派单-message body is " + jsonObject.toJSONString());
        //TODO 处理派单逻辑，插入派单数据，成功则签收消息，否则丢弃消息
        Boolean isInsertSuccess = false;
        if (isInsertSuccess) {
            //手动签收消息，通知MQ 删除此消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } else {
            // 异常处理-丢弃消息
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }
}
