package com.study.mq.hello;

import com.study.MQApplication;
import com.study.mq.publish.PublishProducer;
import com.study.mq.route.RouteProducer;
import com.study.mq.topic.TopicProducer;
import com.study.mq.transaction.OrderProducer;
import com.study.mq.work.WorkProducer;
import com.study.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MQApplication.class)
public class HelloProducerTest {

    @Autowired
    private HelloProducer helloProducer;
    @Test
    public void helloSend() throws Exception {
        helloProducer.send();
    }


    @Autowired
    private WorkProducer workProducer;
    @Test
    public void workSend() throws Exception {
        workProducer.send();
    }

    @Autowired
    private PublishProducer publishProducer;
    @Test
    public void publishSend() throws Exception {
        publishProducer.send();
    }

    @Autowired
    private RouteProducer routeProducer;
    @Test
    public void routeSend() throws Exception {
        routeProducer.send();
    }

    @Autowired
    private TopicProducer topicProducer;
    @Test
    public void topicSend() throws Exception {
        topicProducer.sendTopic1();
        topicProducer.sendTopicN();
    }

    @Autowired
    private OrderProducer orderProducer;
    @Test
    public void saveOrder() throws Exception {
        orderProducer.saveOrder();
    }

    @Autowired
    private OrderService orderService;
    @Test
    public void saveServiceOrder() throws Exception {
        orderService.saveOrder();
    }

}