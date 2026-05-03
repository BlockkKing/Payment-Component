package com.example.backend.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
@ConfigurationProperties(prefix = "cources")
public class EnvVariablesProperties {
        @Value("${courses.rub}")
        private String rub;
        @Value("${courses.dol}")
        private String dol;
        @Value("${courses.eur}")
        private String eur;
}
