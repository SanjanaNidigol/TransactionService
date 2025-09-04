package com.example.transactionservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private String type;
    private LocalDateTime timestamp;
    @Column(nullable = false)
    private String status; // SUCCESS, FAILED

    @Column(nullable = false, length = 3)
    private String currencyCode;
}

//package com.example.transactionservice.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class Transaction {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long transactionId;
//
//    private Long fromAccountId;   // account sending money
//    private Long toAccountId;     // account receiving money
//    private Double amount;
//
//    @Enumerated(EnumType.STRING)
//    private TransactionType type; // DEBIT / CREDIT / TRANSFER
//
//    private LocalDateTime timestamp = LocalDateTime.now();
//}



//package com.example.transactionservice.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//
//@Entity
//@Table(name = "transactions")
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
//public class Transaction {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String fromAccount;
//
//    @Column(nullable = false)
//    private String toAccount;
//
//    @Column(nullable = false, precision = 19, scale = 2)
//    private BigDecimal amount;
//
//    @Column(nullable = false)
//    private Instant createdAt;
//}
