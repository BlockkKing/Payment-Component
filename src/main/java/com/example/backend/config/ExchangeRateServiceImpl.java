package com.example.backend.config;

import com.example.backend.enumeration.CurrencyEnum;
import com.example.backend.service.ExchangeRateService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
//Создание ключа, но при ENUM он больше не нужен
//    private String buildKey(String from, String to) {return from + "_" + to;}

    //Получение актуального курса валют
    @Override
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        validate(fromCurrency, toCurrency);
        return CurrencyEnum.from(fromCurrency, toCurrency).getRate();
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
