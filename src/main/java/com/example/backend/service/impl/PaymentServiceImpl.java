package com.example.backend.service.impl;

import com.example.backend.dto.PaymentRequest;
import com.example.backend.dto.PaymentResponse;
import com.example.backend.listeners.events.PaymentCreatedEvent;
import com.example.backend.mapper.PaymentMapper;
import com.example.backend.model.Fee;
import com.example.backend.model.Payment;
import com.example.backend.model.User;
import com.example.backend.repository.FeeRepository;
import com.example.backend.repository.PaymentRepository;
import com.example.backend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final UserService userService;
    private final ExchangeRateService exchangeRateService;
    private final FeePolicyService feePolicyService;
    private final PaymentRepository paymentRepository;
    private final FeeRepository feeRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PaymentMapper paymentMapper;
    private final NotificationService notificationService;

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
        Fee feeEntity = saveFee(payer, payment, fee);

        eventPublisher.publishEvent(new PaymentCreatedEvent(payment.getId()));
        return paymentMapper.toResponse(payment, feeEntity);
    }

    private Fee saveFee(User payer, Payment payment, BigDecimal feeAmount) {
        Fee fee = new Fee();
        fee.setUser(payer);
        fee.setPayment(payment);
        fee.setAmount(feeAmount);
        return feeRepository.save(fee); // вынести репозиторий
    }

    private void validateUsers(User payer, User recipient) {
        if(payer.getId().equals(recipient.getId())) {
            throw new IllegalArgumentException("Перевод самому себе невозможен");
        }
    }

    private BigDecimal convertToRub(BigDecimal amount, String currency) {
        BigDecimal rate = exchangeRateService.rateForToday(currency, "RUB");
        return amount.multiply(rate)
                .setScale(2, RoundingMode.HALF_UP);
    } // Конвертация к целевой курсу при помощи новой переменной из переменных окружения
    //Исправь и rateForToday
}
