package com.example.backend.config;

import com.example.backend.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final RatesProperties ratesProperties;

    //Получение актуального курса валют
    @Override
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        return getRate(fromCurrency, toCurrency);
    }

    public BigDecimal getRate(String from, String to) {
        validate(from, to);
        String rate = (from + "_" + to).toUpperCase();
        return switch (rate) {
            case "RUB_RUB" -> ratesProperties.getRub();
            case "USD_RUB" -> ratesProperties.getUsd();
            case "EUR_RUB" -> ratesProperties.getEur();
            default -> throw new IllegalArgumentException("Неизвестная конвертация: " + from + " -> " + to);
        };
    }

    //Проверка вход-выход курса на null
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
