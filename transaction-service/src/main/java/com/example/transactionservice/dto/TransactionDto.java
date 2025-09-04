// TransactionDto.java
package com.example.transactionservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import com.example.transactionservice.entity.TransactionType;
import lombok.Data;
@Data
public class TransactionDto {
    private Long transactionId;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private String type; // TRANSFER, DEPOSIT, WITHDRAW
//    private TransactionType type;
    private LocalDateTime timestamp;
    private String status;  // SUCCESS, FAILED
    private String currencyCode;

    private String fromEmail;
    private String toEmail;
}
