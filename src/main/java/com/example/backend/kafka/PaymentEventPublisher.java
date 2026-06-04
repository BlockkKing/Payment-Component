package com.example.backend.kafka;

import com.example.backend.model.PaymentOutbox;
import com.example.backend.repository.PaymentOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {

    @Value("${kafka.payment-events-topic}")
    private String topic;

    private final PaymentOutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void publish() {
        List<PaymentOutbox> events = outboxRepository.pickBatchForPublish();
        if (events.isEmpty()) return;

        for(PaymentOutbox e : events) {
            try {
                // 1) гарантируем eventId
                if(e.getEventId() == null) {
                    e.setEventId(java.util.UUID.randomUUID());
                }

                // 2) фиксируем попытку ДО отправки
                e.setAttempts(e.getAttempts() + 1);
                e.setLastError(null);

                String eventId = e.getEventId().toString();

                ProducerRecord<String, String> record = new ProducerRecord<>(topic, eventId, e.getPayload());

                record.headers().add("event_id", eventId.getBytes(StandardCharsets.UTF_8));
                record.headers().add("event_type", e.getEventType().getBytes(StandardCharsets.UTF_8));
                record.headers().add("aggregate_id", String.valueOf(e.getAggregateId()).getBytes(StandardCharsets.UTF_8));

                // 3) ждём подтверждения от Kafka
                kafkaTemplate.send(record).get();

                // 4) только теперь помечаем как published
                e.setPublishedAt(Instant.now());

                log.info("✅ published outbox id={} eventId={} type={} aggId={}",
                        e.getId(), eventId, e.getEventType(), e.getAggregateId());
            } catch (Exception ex) {
                e.setLastError(shortErr(ex));
                log.warn("❌ failed publish outbox id={} eventId={} error={}",
                        e.getId(),
                        e.getEventId() != null ? e.getEventId() : "null",
                        e.getLastError(), ex);
            }
        }
    }

    private static String shortErr(Exception ex) {
        String msg = ex.getMessage();
        if (msg == null) return ex.getClass().getSimpleName();
        return msg.length() > 500 ? msg.substring(0, 500) : msg;
    }
}
