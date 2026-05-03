package com.example.backend.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FeeDto {
    private Long id;
    private BigDecimal value;
}
