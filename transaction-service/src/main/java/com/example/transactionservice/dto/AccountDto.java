package com.example.transactionservice.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccountDto {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private Long userId;
    private AccountType  accountType;

    public enum AccountType {
        SAVINGS,
        CURRENT,
        FIXED_DEPOSIT,
        SALARY
    }

}
