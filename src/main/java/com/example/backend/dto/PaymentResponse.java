package com.example.backend.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
    public class PaymentResponse {
    Long paymentId;
    BigDecimal amountRub;
    BigDecimal fee;

    /**
     * Идемпотентность: ключ, по которому можно безопасно повторить запрос оплаты.
     * Если клиент не передал ключ в заголовке, он будет сгенерирован сервисом и возвращён здесь.
     */
    private String idempotencyKey;
}
