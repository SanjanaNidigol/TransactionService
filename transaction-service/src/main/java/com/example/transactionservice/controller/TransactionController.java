package com.example.transactionservice.controller;

import com.example.transactionservice.dto.TransactionDto;
import com.example.transactionservice.entity.Transaction;
import com.example.transactionservice.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    // POST transaction
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Map<String, Object> request) {
        Long fromAccountId = request.get("fromAccountId") != null
                ? Long.valueOf(request.get("fromAccountId").toString())
                : null;
        Long toAccountId = request.get("toAccountId") != null
                ? Long.valueOf(request.get("toAccountId").toString())
                : null;
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        String type = request.get("type").toString();

        return ResponseEntity.ok(service.performTransaction(fromAccountId, toAccountId, amount, type));
    }

    // GET all transactions
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(service.getAllTransactions());
    }

    // GET transactions for one account
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsForAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(service.getTransactionsForAccount(accountId));

    }

//    // Get transactions by account
//    @GetMapping("/account/{accountId}")
//    public ResponseEntity<List<TransactionDto>> getTransactionsByAccount(@PathVariable Long accountId) {
//        return ResponseEntity.ok(service.getTransactionsByAccount(accountId));
//    }
//
//    // Get transactions by user
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<TransactionDto>> getTransactionsByUser(@PathVariable Long userId) {
//        return ResponseEntity.ok(service.getTransactionsByUser(userId));
//    }
}

//package com.example.transactionservice.controller;
//
//import com.example.transactionservice.dto.TransactionRequest;
//import com.example.transactionservice.entity.Transaction;
//import com.example.transactionservice.service.TransactionService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/transactions")
//@RequiredArgsConstructor
//public class TransactionController {
//
//    private final TransactionService service;
//
//    @PostMapping("/transfer")
//    public ResponseEntity<?> transfer(@RequestBody TransactionRequest request) {
//        try {
//            Transaction tx = service.transfer(request);
//            return ResponseEntity.ok(tx);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage()); // e.g., "Insufficient balance"
//        }
//    }
//}
//
//
////package com.example.transactionservice.controller;
////
////import com.example.transactionservice.entity.Transaction;
////import com.example.transactionservice.service.TransactionService;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////
////import java.util.List;
////
////@RestController
////@RequestMapping("/transactions")
////public class TransactionController {
////
////    private final TransactionService service;
////
////    public TransactionController(TransactionService service) {
////        this.service = service;
////    }
////
////    // POST - Create new transaction
////    @PostMapping
////    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction tx) {
////        return ResponseEntity.ok(service.createTransaction(tx));
////    }
////
////    // GET - Get transaction by ID
////    @GetMapping("/{id}")
////    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
////        return ResponseEntity.ok(service.getTransaction(id));
////    }
////
////    // GET - Get all transactions for an account
////    @GetMapping("/account/{accountId}")
////    public ResponseEntity<List<Transaction>> getTransactionsByAccount(@PathVariable Long accountId) {
////        return ResponseEntity.ok(service.getTransactionsByAccount(accountId));
////    }
////
////    // GET - Get all transactions (admin use)
////    @GetMapping
////    public ResponseEntity<List<Transaction>> getAllTransactions() {
////        return ResponseEntity.ok(service.getAllTransactions());
////    }
////}
//
//
////package com.example.transactionservice.controller;
////
////import com.example.transactionservice.entity.Transaction;
////import com.example.transactionservice.service.TransactionService;
////import jakarta.validation.constraints.NotBlank;
////import jakarta.validation.constraints.NotNull;
////import lombok.Data;
////import lombok.RequiredArgsConstructor;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////
////import java.math.BigDecimal;
////
////@RestController
////@RequestMapping("/api/transactions")
////@RequiredArgsConstructor
////public class TransactionController {
////    private final TransactionService service;
////
////    @PostMapping("/transfer")
////    public ResponseEntity<Transaction> transfer(@RequestBody TransferRequest req) {
////        return ResponseEntity.ok(service.transfer(req.getFromAccount(), req.getToAccount(), req.getAmount()));
////    }
////
////    @Data
////    public static class TransferRequest {
////        @NotBlank String fromAccount;
////        @NotBlank String toAccount;
////        @NotNull BigDecimal amount;
////    }
////}
