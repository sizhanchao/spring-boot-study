package com.zhan.kafka.service;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author zhan
 * @since 2025-09-14 14:53
 */
@Service
public class KafkaTruncateService {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public void deleteRecordsBeforeOffset(String topic, int partition, long offset)
            throws ExecutionException, InterruptedException {

        // 1. 构建 AdminClient 配置
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        try (AdminClient adminClient = AdminClient.create(props)) {

            // 2. 创建 TopicPartition
             TopicPartition tp = new TopicPartition(topic, partition);

            // 3. 设置要删除到的 offset（注意：是删除 offset < 给定值 的记录）
            // 即：保留 offset >= 3 的消息，删除 offset < 3 的消息
            Map<TopicPartition, RecordsToDelete> recordsToDelete = new HashMap<>();
            recordsToDelete.put(tp, RecordsToDelete.beforeOffset(offset));

            // 4. 调用 deleteRecords
            DeleteRecordsResult result = adminClient.deleteRecords(recordsToDelete);

            // 5. 等待结果
            Map<TopicPartition, KafkaFuture<DeletedRecords>> futureResults = result.lowWatermarks();
            for (Map.Entry<TopicPartition, KafkaFuture<DeletedRecords>> entry : futureResults.entrySet()) {
                DeletedRecords deletedRecords = entry.getValue().get(); // 阻塞等待
                System.out.println("✅ 分区 " + entry.getKey() + " 已删除到 offset: " + deletedRecords.lowWatermark());
            }

        }
    }

}
