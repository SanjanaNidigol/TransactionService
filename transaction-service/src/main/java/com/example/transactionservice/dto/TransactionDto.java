// TransactionDto.java
package com.example.transactionservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import lombok.Data;
@Data
public class TransactionDto {
    private Long id;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private String type; // TRANSFER, DEPOSIT, WITHDRAW
    private LocalDateTime timestamp;
    private String status; // SUCCESS, FAILED

}
