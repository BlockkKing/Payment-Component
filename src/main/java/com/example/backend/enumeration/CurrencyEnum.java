package com.example.backend.enumeration;

import java.math.BigDecimal;

public enum CurrencyEnum {
    USD_RUB(new BigDecimal("75")),
    EUR_RUB(new BigDecimal("89")),
    RUB_RUB(BigDecimal.ONE);

    private final BigDecimal rate;

    CurrencyEnum(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public static CurrencyEnum from(String from, String to) {
        try {
            return CurrencyEnum.valueOf((from + "_" + to).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неизвестная конвертация: " + from + " -> " + to);
        }
     }
}
