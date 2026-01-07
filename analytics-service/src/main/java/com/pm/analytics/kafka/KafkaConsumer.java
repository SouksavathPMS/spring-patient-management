package com.pm.analytics.kafka;


import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Slf4j
@Service
public class KafkaConsumer {
    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consume(byte[] events) {
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(events);
            // ... perform any business related to analytics here
            log.info("Received patient event: [PatientId={}, PatientName={}, PatientEmail={}]", patientEvent.getPatientId(), patientEvent.getName(), patientEvent.getEmail());
        } catch (Exception e) {
            log.error("Error deserializing event {}", e.getMessage());
        }
    }
}
