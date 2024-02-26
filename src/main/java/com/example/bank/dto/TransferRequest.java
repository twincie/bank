package com.example.bank.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
    private String accountNumber;
    private BigDecimal amount;
}
