package com.example.backend.enumeration;

import java.math.BigDecimal;

public enum Currency {
    USD_RUB(new BigDecimal("75")),
    EUR_RUB(new BigDecimal("89")),
    RUB_RUB(BigDecimal.ONE);

    private final BigDecimal rate;

    Currency(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public static Currency from(String from, String to) {
        try {
            return Currency.valueOf((from + "_" + to).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неизвестная конвертация: " + from + " -> " + to);
        }
     }
}
