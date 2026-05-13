package com.example.backend.listeners;

import com.example.backend.listeners.events.PaymentCreatedEvent;
import com.example.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentListener {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payment-topic", groupId = "payment-group")
    public void handle(String payload) throws Exception {
        PaymentCreatedEvent event = objectMapper.readValue(payload, PaymentCreatedEvent.class);

        notificationService.notifyPaymentCreated(event.paymentId());

        log.info("Processed payment event {}", event.paymentId());
    }
}
