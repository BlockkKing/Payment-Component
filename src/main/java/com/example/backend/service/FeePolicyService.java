package com.example.backend.service;

import java.math.BigDecimal;

/**
 * ,бизнес-правила коммисии
 */

public interface FeePolicyService {
    BigDecimal calculateFee(BigDecimal amountRub);
}
