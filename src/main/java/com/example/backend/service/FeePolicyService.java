package com.example.backend.service;

import java.math.BigDecimal;

public interface FeePolicyService {
    BigDecimal calculateFee(BigDecimal amountRub);
}
