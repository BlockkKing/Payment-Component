package com.example.backend.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
@Setter
@ConfigurationProperties(prefix = "rates")
public class RatesProperties {
        private String rub;
        private String usd;
        private String eur;
}
