package com.example.backend.service;

import com.example.backend.dto.PaymentRequest;
import com.example.backend.dto.PaymentResponse;

public interface PaymentService {
    PaymentResponse pay(PaymentRequest request);
}
