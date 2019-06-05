package com.study.listen;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.study.mapper.OrderMapper;
import com.study.model.OrderInfoModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description: 补单消费者
 * @Author mengfanzhu
 * @Date 2019/6/4 15:06
 * @Version 1.0
 */
@Component
@Slf4j
public class ReCreateOrderConsumer {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 补单队列
     */
    public static final String CREATE_QUEUE_NAME_TRANSACTION = "api.order.reCreate.queue";

    /**
     * @param message
     * @param headers
     * @param channel
     */
    @RabbitListener(queues = CREATE_QUEUE_NAME_TRANSACTION)
    public void orderReceiver(Message message, @Headers Map<String, Object> headers, Channel channel) {
        try {
            log.info("supplement-message；{}", JSONObject.toJSONString(message));
            String msgBody = new String(message.getBody(), "UTF-8");
            OrderInfoModel infoModel = JSONObject.parseObject(msgBody,OrderInfoModel.class);
            log.info("supplement-message body is {} ", infoModel);

            String orderNo = infoModel.getOrderNo();
            OrderInfoModel orderInfoModel = orderMapper.getByOrderNo(orderNo);
            if (null == orderInfoModel) {
               Long result = orderMapper.insert(infoModel);
               if(result != 1L){
                   throw new Exception("ReCreateOrderInfo Failed!");
               }
            }
            log.info("手动签收消息，通知MQ 删除此消息");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("supplement error {}", e.toString(), e);
        }
    }
}
