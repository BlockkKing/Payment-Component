package com.example.backend.config;

import com.example.backend.service.FeePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
@Service
public class FeePolicyServiceImpl implements FeePolicyService {

//    private final RatesProperties ratesProperties;
//    Аналогичный метод в ExchangeRateServiceImpl - getExchangeRate
//    private String getCourse(String currency) {
//        return switch (currency.toUpperCase()) {
//            case "RUB" -> ratesProperties.getRub();
//            case "USD" -> ratesProperties.getUsd();
//            case "EUR" -> ratesProperties.getEur();
//            default -> throw new IllegalArgumentException("Нет подходящего курса: " + currency);
//        };}

    private final FeeProperties feeProperties;

    @Override
    public BigDecimal calculateFee(BigDecimal amountRub) {
        validate(amountRub); // Валидация входа
        BigDecimal rate = resolveRate(amountRub); // Определение ставки
        return amountRub.multiply(rate) // расчет комисии
                .setScale(2, RoundingMode.HALF_UP); // округление до 2 занков после запятой
    }

    private BigDecimal resolveRate(BigDecimal amountRub) {
        if (amountRub.compareTo(feeProperties.getThresholdLow()) < 0) { // нам подходит вариант только когда меньше 1000
            return feeProperties.getRateLow();
        } else if (amountRub.compareTo(feeProperties.getThresholdHigh()) <= 0){ // нам подходит вариант когда меньше 5, но больше 1000 или равно 5
            return feeProperties.getRateMid();
        } else {
            return feeProperties.getRateHigh();
        }
    }

    private void validate(BigDecimal amountRub) {
        if(amountRub == null || amountRub.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Баланс должен положительным");
        }
    }
}
