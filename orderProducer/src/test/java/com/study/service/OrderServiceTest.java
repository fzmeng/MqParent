package com.study.service;

import com.study.MQApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MQApplication.class)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Test
    public void saveServiceOrder() throws Exception {
        orderService.saveOrder();
    }

}