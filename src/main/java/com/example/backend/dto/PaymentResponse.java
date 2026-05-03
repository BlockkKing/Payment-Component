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
}
