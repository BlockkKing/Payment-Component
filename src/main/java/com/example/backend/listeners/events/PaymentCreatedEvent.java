package com.example.backend.listeners.events;

import java.math.BigDecimal;

public record PaymentCreatedEvent (Long paymentId,
                                    Long payerId,
                                   Long recipientId,
                                   BigDecimal amountRub) {
}
