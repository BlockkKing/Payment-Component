package com.example.backend.service;

import java.math.BigDecimal;

public interface ExchangeRateService {
    BigDecimal rateForToday(String fromCurrency, String toCurrency);
}
