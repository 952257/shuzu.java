package com.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RocketMQMessageListener(topic = "orderTopic", consumerGroup = "ordered-consumer", consumeMode = ConsumeMode.ORDERLY)
public class OrderedMsgConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        log.info("consumer 顺序消费，收到消息{}", message);
    }
}
