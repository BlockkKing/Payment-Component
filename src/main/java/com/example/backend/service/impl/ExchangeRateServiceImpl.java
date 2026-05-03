package com.example.backend.service.impl;
import com.example.backend.service.ExchangeRateService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.util.Map;

/**
 * Тут сделали Мапу ключей по котором будет понятен текущий курс по фильтру
 */
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private static final Map<String, BigDecimal> RATES = Map.of(
            "USD_RUB", new BigDecimal("75"),
            "EUR_RUB", new BigDecimal("89"),
            "RUB_RUB", BigDecimal.ONE
    ); // подумай про enum + переменные окружения

    //Получение актуального курса
    @Override
    public BigDecimal rateForToday(String fromCurrency, String toCurrency) {
        validate(fromCurrency, toCurrency);
        String key = buildKey(fromCurrency, toCurrency).toUpperCase();
        BigDecimal rate = RATES.get(key);
        if(rate == null) {
            throw new IllegalArgumentException("Unsapported currency: " + fromCurrency);
        }

        return rate;
    }

    //Создание ключа
    private String buildKey(String from, String to) {
        return from + "_" + to;
    }

    //Проверка
    private void validate(String from, String to) {
        // isBlank - строка из пробелов считается пустой, а в isEmpty - нет
        if(from == null || from.isBlank()) {
            throw new IllegalArgumentException("Source currency is empty");
        }
        if(to == null || to.isBlank()) {
            throw new IllegalArgumentException("Target currency is empty");
        }
    }
}
