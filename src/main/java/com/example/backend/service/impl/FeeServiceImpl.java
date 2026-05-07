package com.example.backend.service.impl;

import com.example.backend.model.Fee;
import com.example.backend.model.Payment;
import com.example.backend.model.User;
import com.example.backend.repository.FeeRepository;
import com.example.backend.service.FeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class FeeServiceImpl implements FeeService {

    private final FeeRepository feeRepository;

    @Override
    public Fee createFee(User payer, Payment payment, BigDecimal amount) {
        Fee fee = new Fee(payer, payment, amount);
        return feeRepository.save(fee);
    }


}
