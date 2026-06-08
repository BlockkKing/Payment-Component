package com.example.backend.mapper;

import com.example.backend.dto.PaymentRequest;
import com.example.backend.model.Fee;
import com.example.backend.model.Payment;
import com.example.backend.dto.PaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentRequest request) {
        if (request == null) return null;
        return new Payment();
    }

    public PaymentResponse toResponse(Payment payment, Fee fee) {
        if(payment == null) throw new IllegalArgumentException("Платёж не может быть равен null");

        return new PaymentResponse(
                payment.getId(),
                payment.getAmountRub(),
                fee != null ? fee.getAmount() : null,
                payment.getIdempotencyKey()
        );
    }
}
