package com.zhan.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @author zhan
 * @since 2025-08-17 15:59
 */
@Service
public class KafkaConsumer {

    @KafkaListener(topics = "test-topic",groupId = "my-group")
    public void listen(ConsumerRecord<String, String> record) {
        System.out.printf("Received message: topic - %s, partition - %d, offset - %d, key - %s, value - %s%n",
                record.topic(), record.partition(), record.offset(), record.key(), record.value());
    }
}
