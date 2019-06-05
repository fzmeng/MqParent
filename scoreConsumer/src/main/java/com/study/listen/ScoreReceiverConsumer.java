package com.study.listen;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.study.mapper.ScoreMapper;
import com.study.model.ScoreModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Description: 积分 消费者
 * @Author mengfanzhu
 * @Date 2019/6/4 15:06
 * @Version 1.0
 */
@Component
@Slf4j
public class ScoreReceiverConsumer {

    @Autowired
    private ScoreMapper scoreMapper;

    /**
     * 积分队列
     */
    public static final String QUEUE_NAME_TRANSACTION = "api.score.queue";

    @Bean
    public Queue scoreQueue() {
        return new Queue(QUEUE_NAME_TRANSACTION, true);
    }

    @RabbitListener(queues = QUEUE_NAME_TRANSACTION)
    public void orderReceiver(Message message, @Headers Map<String, Object> headers, Channel channel) {
        try {
            log.info("score-message:{}", JSONObject.toJSONString(message));
            String msgBody = new String(message.getBody(), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(msgBody);
            String orderNo = jsonObject.getString("orderNo");
            log.info("score-message body is :{}" + jsonObject.toJSONString());
            //幂等处理
            ScoreModel scoreModel = scoreMapper.getByOrderNo(orderNo);
            if (scoreModel != null) {
                //丢弃消息
                log.info("repeat consumer ，discard the message.");
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
            //处理积分逻辑，插入积分数据，成功则签收消息
            scoreModel = new ScoreModel();
            scoreModel.setOrderNo(orderNo);
            scoreModel.setScore(100);
            Boolean isInsertSuccess = scoreMapper.insert(scoreModel) == 1L;
            if (isInsertSuccess) {
                //手动签收消息，通知MQ 删除此消息
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                log.info("scoreConsumer handler success.");
            }
        } catch (IOException e) {
            log.error("scoreConsumer handler Faild,{}", e.toString(), e);
        }
    }
}
