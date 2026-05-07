package com.example.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConfigurationProperties(prefix = "fees")
@Getter
@Setter
public class FeeProperties {

    private BigDecimal thresholdHigh;
    private BigDecimal thresholdLow;

    private BigDecimal rateLow;
    private BigDecimal rateMid;
    private BigDecimal rateHigh;
}
