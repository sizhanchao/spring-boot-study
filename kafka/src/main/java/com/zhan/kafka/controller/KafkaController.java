package com.zhan.kafka.controller;

import com.zhan.kafka.producer.KafkaProducer;
import com.zhan.kafka.service.KafkaTruncateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

/**
 * @author zhan
 * @since 2025-08-17 16:01
 */
@RestController
@RequestMapping("/kafka")
public class KafkaController {
    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    KafkaTruncateService kafkaTruncateService;

    @PostMapping("/send")
    public String sendMessage(@RequestParam String msg) {
        kafkaProducer.sendMessage("test-topic", msg);
        return "Message sent: " + msg;
    }

    @GetMapping("delMessage")
    public void delMessage(@RequestParam long offset) throws ExecutionException, InterruptedException {
        kafkaTruncateService.deleteRecordsBeforeOffset("test-topic", 0, offset);
    }

}
