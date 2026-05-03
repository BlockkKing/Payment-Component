package com.example.backend.service.impl;

import com.example.backend.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void notifyPaymentCreated(Long paymentId) {
            log.info("Уведомление отправлено: создан платеж с id={}", paymentId);
    }
}
