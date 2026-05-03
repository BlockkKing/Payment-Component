package com.example.backend.service.impl;

import com.example.backend.service.FeePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class FeePolicyServiceImpl implements FeePolicyService {

    private static final BigDecimal THRESHOLD_LOW = new BigDecimal("1000");
    private static final BigDecimal THRESHOLD_HIGH = new BigDecimal("5000");

    private static final BigDecimal RATE_LOW = new BigDecimal("0.015"); // 1.5%
    private static final BigDecimal RATE_MID = new BigDecimal("0.01"); // 1.0%
    private static final BigDecimal RATE_HIGH = new BigDecimal("0.005"); // 0.5%

    private final EnvVariablesProperties courseProperties;

    //перемеиновать EnvVariablesProperties в CourseProperties
    //Вынести переменные в переменные окружения

    @Override
    public BigDecimal calculateFee(BigDecimal amountRub) {
        validate(amountRub); // Валидация входа

        BigDecimal rate = resolveRate(amountRub); // Определение ставки

        return amountRub.multiply(rate) // расчет комисии
                .setScale(2, RoundingMode.HALF_UP); // округление до 2 занков после запятой
    }

    private BigDecimal resolveRate(BigDecimal amountRub) {
        if (amountRub.compareTo(THRESHOLD_LOW) < 0) { // нам подходит вариант только когда меньше 1000
            return RATE_LOW;
        } else if (amountRub.compareTo(THRESHOLD_HIGH) <= 0){ // нам подходит вариант когда меньше 5, но больше 1000 или равно 5
            return RATE_MID;
        } else {
            return RATE_HIGH;
        }
    }

    private String getCourse(String currency) {
        return switch (currency.toUpperCase()) {
            case "RUB" -> courseProperties.getRub();
            case "DOL" -> courseProperties.getDol();
            case "EUV" -> courseProperties.getEur();
            default -> throw new IllegalArgumentException("Нет подходящего курса: " + currency);
        };
    }

    private void validate(BigDecimal amountRub) {
        if(amountRub == null || amountRub.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Баланс должен положительным");
        }
    }

}
