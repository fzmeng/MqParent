RabbitMQ + SpringBoot 实现分布式事务场景 -下单 + 积分处理
================
> ## 分布式事务应用场景：
> * 【分库分表时】- 服务内存在跨数据库场景；
> * 【应用SOA化】- 不同服务容器之间服务调用场景；
> 用户注册service -> 调用【积分服务-赠送积分service】；
> 用户下单service -> 调用【扣减用户积分service】or【扣减用户消费券service】or 【派单service】；
> 用户支付service -> 调用【更新订单service】+【账户记账service】+【增加用户积分service】;
> 。。。
> ### 如何使用RabbitMQ 解决分布式事务-最终一致性：
> * 确认Producer 一定将消息投递到MQ服务中；
> * 消息持久化；
> * 确认Consumer  一定将消息消费了，ACK手动应答模式（消息补偿-考虑消息处理幂等）；

### [详细介绍](https://blog.csdn.net/mengfanzhundsc/article/details/90813642)