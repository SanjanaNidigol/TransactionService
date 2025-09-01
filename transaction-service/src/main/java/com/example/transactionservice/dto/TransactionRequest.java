package com.example.transactionservice.dto;

import lombok.Data;

@Data
public class TransactionRequest {
    private Long fromAccountId;
    private Long toAccountId;
    private Double amount;
    private String type;   // e.g., TRANSFER, DEPOSIT, WITHDRAW
}
