package com.example.backend.service.impl;

import com.example.backend.model.PaymentOutbox;
import com.example.backend.repository.PaymentOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxPublisherService {

    private final PaymentOutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void publish() {
        List<PaymentOutbox> events = outboxRepository.findByPublishedAtIsNull();

        for(PaymentOutbox event : events) {
            try {
                kafkaTemplate.send("payment-topic", event.getPayload());
                event.setPublishedAt(Instant.now());
            } catch (Exception e) {
                event.setAttempts(event.getAttempts() + 1);
                event.setLastError(e.getMessage());

                log.error("Kafka publish error", e);
            }
        }
    }
}
