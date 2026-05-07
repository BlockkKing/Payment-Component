package com.example.backend.service;

import com.example.backend.model.Fee;
import com.example.backend.model.Payment;
import com.example.backend.model.User;

import java.math.BigDecimal;

public interface FeeService {
    Fee createFee(User payer, Payment payment, BigDecimal amount);
}
