package com.example.backend.service.impl;

import com.example.backend.dto.PaymentRequest;
import com.example.backend.dto.PaymentResponse;
import com.example.backend.mapper.PaymentMapper;
import com.example.backend.model.Fee;
import com.example.backend.model.Payment;
import com.example.backend.model.PaymentOutbox;
import com.example.backend.model.User;
import com.example.backend.repository.PaymentOutboxRepository;
import com.example.backend.repository.PaymentRepository;
import com.example.backend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final UserService userService;
    private final ExchangeRateService exchangeRateService;
    private final FeePolicyService feePolicyService;
    private final PaymentRepository paymentRepository;
    private final FeeService feeService;
    private final PaymentMapper paymentMapper;
    private final PaymentOutboxRepository paymentOutboxRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public PaymentResponse pay(PaymentRequest request) {
        // 1. Получить отправителя
        //User payer = findUser(request.payerId());
        User payer = userService.findById(request.getPayerId());
        // 2. Получить получателя
        //User recipient = findUser(request.recipientId());
        User recipient = userService.findById(request.getRecipientId());
        // 3. Проверка данных пользователя
        validateUsers(payer, recipient);
        // 4. Конвертация и получение курса валюты
        BigDecimal amountRub = convertToRub(request.getAmount(), request.getCurrencyCode());
        // 5. Подсчет комиссии
        BigDecimal fee = feePolicyService.calculateFee(amountRub);
        // 6. Сохранение платежа + логирование
        Payment payment = paymentMapper.toEntity(request);
        payment.setPayer(payer);
        payment.setRecipient(recipient);
        payment.setAmountRub(amountRub);
        payment.setBookingDate(LocalDateTime.now());
        payment = paymentRepository.save(payment);
        // 7. Сохранение комиссии
        Fee feeEntity = feeService.createFee(payer, payment, fee);

        paymentOutboxRepository.save(outboxEvent(payment, Map.of(
                "paymentId", payment.getId(),
                "amount", payment.getAmountRub(),
                "payerId", payer.getId(),
                "recipientId", recipient.getId()
        )));

        return paymentMapper.toResponse(payment, feeEntity);
    }

    private String toJson(Object obj) {
        try{
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new IllegalArgumentException("Не удалось сериализовать outbox payload", e);
        }
    }

    private PaymentOutbox outboxEvent(Payment payment, Map<String, Object> payload) {
        return PaymentOutbox.builder()
                .aggregateId(payment.getId())
                .eventId(UUID.randomUUID())
                .eventType("PAYMENT_CREATED")
                .payload(toJson(payload))
                .createdAt(Instant.now())
                .publishedAt(null)
                .attempts(0)
                .lastError(null)
                .build();
    }

    private void validateUsers(User payer, User recipient) {
        if(payer.getId().equals(recipient.getId())) {
            throw new IllegalArgumentException("Перевод самому себе невозможен");
        }
    }

    private BigDecimal convertToRub(BigDecimal amount, String currency) {
        BigDecimal rate = exchangeRateService.getExchangeRate(currency, "RUB");
        return amount.multiply(rate)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
