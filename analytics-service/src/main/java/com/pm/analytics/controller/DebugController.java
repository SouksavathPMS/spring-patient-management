package com.pm.analytics.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @GetMapping("/kafka-config")
    public Map<String, String> getKafkaConfig() {
        Map<String, String> config = new HashMap<>();
        log.info("Kafka config is {}", bootstrapServers);
        config.put("bootstrap-servers", bootstrapServers);
        return config;
    }
}