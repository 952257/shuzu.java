package com.rocketmq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RocketMQMessageListener(topic = "myTest", consumerGroup = "myTest")
public class OrdeConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String o) {
        log.info("o:" + JSON.toJSONString(o));
    }
}