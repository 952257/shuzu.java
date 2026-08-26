package com.rocketmq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class ProducerController {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @RequestMapping("/sendMsg")
    public String sendMsg() {
        OrderEntity orderEntity = new OrderEntity(123456,"发送消息");
        // 第一种方式：普通发送
        rocketMQTemplate.convertAndSend("myTest", JSON.toJSONString(orderEntity));
        // 第二种方式：封装消息体发送
        /*Message<String> message = new GenericMessage<>("hi zhengzhou");
        rocketMQTemplate.convertAndSend("myTest",message);*/
        
        return "success";
    }

    @RequestMapping("/sendSyncMsg")
    public String sendSyncMsg() {
        OrderEntity orderEntity = new OrderEntity(123456, "同步发送消息");
        for (int i = 0; i < 10; i++) {
            // 发送同步消息
            SendResult myTest = rocketMQTemplate.syncSend("myTest", JSON.toJSONString(orderEntity));
            log.info(i + "-" + myTest.getSendStatus());
        }
        return "success";
    }

    @RequestMapping("/sendASyncMsg")
    public String sendASyncMsg() {
        OrderEntity orderEntity = new OrderEntity(123456, "异步发送消息");
        for (int i = 0; i < 10; i++) {
            int index = i;
            rocketMQTemplate.asyncSend("myTest", JSON.toJSONString(orderEntity), new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    //成功回调
                   log.info(index+"-"+sendResult.getSendStatus());
                }
                @Override
                public void onException(Throwable e) {
                    //异常回调
                    log.info(e.getMessage());
                }
            });
        }
        return "success";
    }

    @RequestMapping("/sendDelayMsg")
    public String sendAsyncMsgByJsonDelay() throws RemotingException, InterruptedException, MQClientException {
        OrderEntity orderEntity = new OrderEntity(123456, "异步延迟消息");

        //消息内容将orderExt转为json
        String json = JSON.toJSONString(orderEntity);
        Message message =
                new Message("delayTopic",json.getBytes(StandardCharsets.UTF_8));
        //设置延迟等级
        message.setDelayTimeLevel(4);
        //发送异步消息
        this.rocketMQTemplate.getProducer().send(message,new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println(sendResult);
            }
            @Override
            public void onException(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }
        });

        System.out.printf("send msg : %s",orderEntity);
        return "success";
    }

    @RequestMapping("/sendBatch")
    public String sendBatch() {
        List<Integer> ids = List.of(1001, 1002, 1003, 1004, 1005);
        // <1>  创建多条 Demo02Message 消息
        List<org.springframework.messaging.Message> messages =
                new ArrayList<>(ids.size());
        for (long id : ids) {
            // 创建 Demo02Message 消息
            OrderEntity message = new OrderEntity();
            message.setId(id);
            // 构建 Spring Messaging 定义的 Message 消息
            messages.add(MessageBuilder.withPayload(message).build());
        }
        rocketMQTemplate.syncSend("batchTopic", messages, 30 * 1000L);
        return "success";
    }
}
