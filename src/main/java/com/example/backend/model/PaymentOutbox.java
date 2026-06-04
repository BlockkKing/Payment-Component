package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payment_outbox")
public class PaymentOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //primary key

    @Column(name = "aggregate_id", nullable = false)
    private Long aggregateId; // id payment

    @Column(name = "event_id", nullable = false)
    private UUID eventId; // deduplication после retry может быть, чтобы понимать это

    @Column(name = "event_type", nullable = false)
    private String eventType; // тип события

    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    private String payload; // сериализация в json

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "attempts", nullable = false)
    private int attempts; // защита от бесконечных retry

    @Column(name = "last_error", length = 4000)
    private String lastError; // хранит последнюю ошибку отправки

    // гарантия уникального uuid, если забыли задать
    @PrePersist
    void prePersist() {
        if (eventId == null) {
            eventId = UUID.randomUUID();
        }
    }
}
