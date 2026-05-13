package com.example.backend.repository;

import com.example.backend.model.PaymentOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentOutboxRepository extends JpaRepository<PaymentOutbox, Long> {

    List<PaymentOutbox> findByPublishedAtIsNull();

}
