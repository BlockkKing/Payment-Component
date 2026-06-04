package com.example.backend.repository;

import com.example.backend.model.PaymentOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentOutboxRepository extends JpaRepository<PaymentOutbox, Long> {
    @Query(value = "select * from ms_payment_component.payment_outbox where published_at is null order by id asc limit 50 for update skip locked", nativeQuery = true)
    List<PaymentOutbox> pickBatchForPublish();
}
