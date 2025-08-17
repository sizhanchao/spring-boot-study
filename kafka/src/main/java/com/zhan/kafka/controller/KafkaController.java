package com.zhan.kafka.controller;

import com.zhan.kafka.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhan
 * @since 2025-08-17 16:01
 */
@RestController
@RequestMapping("/kafka")
public class KafkaController {
    @Autowired
    private KafkaProducer kafkaProducer;

    @PostMapping("/send")
    public String sendMessage(@RequestParam String msg) {
        kafkaProducer.sendMessage("test-topic", msg);
        return "Message sent: " + msg;
    }
}
