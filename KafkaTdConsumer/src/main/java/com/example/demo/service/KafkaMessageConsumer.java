package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import java.util.Properties;

import com.taosdata.jdbc.TSDBDriver;


@Component
public class KafkaMessageConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaMessageConsumer.class);

    @KafkaListener(topics={"${kafka.app.topic.foo}"})
    public void receive(@Payload String message, @Headers MessageHeaders headers){
        TSDBDate tsdbDate =new TSDBDate();
        LOG.info("KafkaMessageConsumer 接收到消息："+message);
        headers.keySet().forEach(key->LOG.info("{}: {}",key,headers.get(key)));
        tsdbDate.doMakeJdbcUrl();
//        插入数据
        if (message != null){
            String mes=message.toString();
            tsdbDate.doInsert(System.currentTimeMillis(),mes);
        }
    }
}
