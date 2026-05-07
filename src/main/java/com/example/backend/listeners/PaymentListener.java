package com.example.backend.listeners;

import com.example.backend.listeners.events.PaymentCreatedEvent;
import com.example.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentListener {

    private final NotificationService notificationService;

    //Сообщение должно появиться в консоли
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PaymentCreatedEvent event) {
        log.debug("Создан платёж с id = {}", event.paymentId()); // если не будет сообщения, добавить в yml уровень логирования через указание пакета, добавь debug

        try {
            notificationService.notifyPaymentCreated(event.paymentId());
        } catch (Exception e) {
            log.error("Ошибка отправки уведомления, paymentId = {}", event.paymentId(), e);
        }

    }
}
