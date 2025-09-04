package com.example.transactionservice.dto;

import com.example.transactionservice.entity.TransactionType;
import lombok.Data;

@Data
public class TransactionRequest {
    private Long fromAccountId;
    private Long toAccountId;
    private Double amount;
    private String type;   // e.g., TRANSFER, DEPOSIT, WITHDRAW
//    private TransactionType type;
    private String currencyCode;
}
