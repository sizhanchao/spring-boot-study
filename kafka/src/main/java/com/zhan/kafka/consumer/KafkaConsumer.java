package com.zhan.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @author zhan
 * @since 2025-08-17 15:59
 */
@Service
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "test-topic",groupId = "my-group")
    public void listen(ConsumerRecord<String, String> record) {
        System.out.printf("Received message: topic - %s, partition - %d, offset - %d, key - %s, value - %s%n",
                record.topic(), record.partition(), record.offset(), record.key(), record.value());
    }

    /**
     * 监听所有消息，自动打印
     */
    @KafkaListener(topics = "test-topic", groupId = "log-consumer-group")
    public void listenLog(ConsumerRecord<String, String> record) {
        String topic = record.topic();
        int partition = record.partition();
        long offset = record.offset();
        String key = record.key();
        String value = record.value();
        long timestamp = record.timestamp();

        // 格式化输出
        String logMessage = String.format(
                """
                📬 Kafka 消息日志
                -------------------------
                Topic:      %s
                Partition:  %d
                Offset:     %d
                Timestamp:  %d (%tF %tT)
                Key:        %s
                Value:      %s
                -------------------------
                """,
                topic, partition, offset, timestamp, timestamp, timestamp, key, value
        );

        // 打印到控制台
        System.out.println(logMessage);

        // 同时记录到日志文件（如 logback）
        logger.info("Kafka消息: topic={}, partition={}, offset={}, key={}, value={}",
                topic, partition, offset, key, value);
    }
}
