package com.pm.patient_service.kafka;

import com.pm.patient_service.model.Patient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private static final String TOPIC = "patient";

    public void sendEvent(Patient patient) {
        try {
            PatientEvent event = PatientEvent.newBuilder()
                    .setPatientId(patient.getId().toString())
                    .setName(patient.getName())
                    .setEmail(patient.getEmail())
                    .setEventType("PATIENT_CREATED")
                    .build();

            byte[] eventBytes = event.toByteArray();

            // Send with callback
            CompletableFuture<SendResult<String, byte[]>> future =
                    kafkaTemplate.send(
                            TOPIC,
                            patient.getId().toString(),
                            eventBytes
                    );

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Successfully sent PatientEvent for patientId: {} to partition: {} with offset: {}",
                            patient.getId(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                } else {
                    log.error("Failed to send PatientEvent for patientId: {}", patient.getId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error creating PatientEvent for patientId: {}", patient.getId(), e);
            throw new RuntimeException("Failed to send patient event", e);
        }
    }
}