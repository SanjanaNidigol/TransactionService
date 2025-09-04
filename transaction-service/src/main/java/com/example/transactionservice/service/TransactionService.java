package com.example.transactionservice.service;

import com.example.transactionservice.client.AccountClient;
import com.example.transactionservice.dto.AccountDto;
import com.example.transactionservice.entity.Transaction;
import com.example.transactionservice.entity.TransactionType;
import com.example.transactionservice.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    private final TransactionRepository repo;
    private final AccountClient accountClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public TransactionService(TransactionRepository repo, AccountClient accountClient, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.repo = repo;
        this.accountClient = accountClient;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public Transaction performTransaction(Long fromAccountId, Long toAccountId, BigDecimal amount, String type, String currencyCode) {
        Transaction tx = new Transaction();
        tx.setFromAccountId(fromAccountId);
        tx.setToAccountId(toAccountId);
        tx.setAmount(amount);
        tx.setType(type);
        tx.setTimestamp(LocalDateTime.now());
        tx.setCurrencyCode(currencyCode);

        try {
            if (type.equalsIgnoreCase("TRANSFER")) {
                AccountDto fromAcc = accountClient.getAccountById(fromAccountId);
                if (fromAcc == null || fromAcc.getBalance().compareTo(amount) < 0) {
                    tx.setStatus("FAILED");
                    return repo.save(tx);
                }
                accountClient.debit(fromAccountId, amount);
                accountClient.credit(toAccountId, amount);
            } else if (type.equalsIgnoreCase("DEBIT")) {
                AccountDto fromAcc = accountClient.getAccountById(fromAccountId);
                if (fromAcc == null || fromAcc.getBalance().compareTo(amount) < 0) {
                    tx.setStatus("FAILED");
                    return repo.save(tx);
                }
                accountClient.debit(fromAccountId, amount);
            } else if (type.equalsIgnoreCase("CREDIT")) {
                accountClient.credit(toAccountId, amount);
            }

            tx.setStatus("SUCCESS");

        } catch (Exception e) {
            tx.setStatus("FAILED");
        }

        Transaction savedTx = repo.save(tx);

        try {
            Map<String, Object> event = new HashMap<>();
            event.put("transactionId", savedTx.getTransactionId());
            event.put("type", type);
            event.put("fromAccountId", fromAccountId);
            event.put("toAccountId", toAccountId);
            event.put("amount", amount.toPlainString());
            event.put("status", savedTx.getStatus());
            event.put("currencyCode", currencyCode);

            String message = objectMapper.writeValueAsString(event);
            String eventJson = new ObjectMapper().writeValueAsString(event);
            kafkaTemplate.send("transaction-events", eventJson);
            System.out.println("✅ Sent transaction event: " + message);

        } catch (Exception e) {
            e.printStackTrace(); // log properly in real app
        }
        // Kafka message
//        String message = String.format("TRANSACTION:%d,type=%s,from=%d,to=%d,amount=%s,status=%s,currency=%s",
//                savedTx.getTransactionId(), type, fromAccountId, toAccountId, amount.toPlainString(), savedTx.getStatus(), currencyCode);
//        kafkaTemplate.send("transaction-events", message);

        return savedTx;
    }


//    public Transaction performTransaction(Long fromAccountId, Long toAccountId, BigDecimal amount, String type) {
//        if (type.equalsIgnoreCase("TRANSFER")) {
//            // Get source account and check balance
//            AccountDto fromAcc = accountClient.getAccountById(fromAccountId);
//            if (fromAcc == null || fromAcc.getBalance().compareTo(amount) < 0) {
//                throw new RuntimeException("Insufficient balance for transfer");
//            }
//            accountClient.debit(fromAccountId, amount);
//            accountClient.credit(toAccountId, amount);
//        } else if (type.equalsIgnoreCase("DEBIT")) {
//            AccountDto fromAcc = accountClient.getAccountById(fromAccountId);
//            if (fromAcc == null || fromAcc.getBalance().compareTo(amount) < 0) {
//                throw new RuntimeException("Insufficient balance for debit");
//            }
//            accountClient.debit(fromAccountId, amount);
//        } else if (type.equalsIgnoreCase("CREDIT")) {
//            accountClient.credit(toAccountId, amount);
//        }
//
//        // Save transaction
//        Transaction tx = new Transaction();
//        tx.setFromAccountId(fromAccountId);
//        tx.setToAccountId(toAccountId);
//        tx.setAmount(amount);
//        tx.setType(type);
//        tx.setTimestamp(LocalDateTime.now());
//
//        Transaction savedTx = repo.save(tx);
//
//        // Publish transaction event to Kafka
//        String message = String.format("TRANSACTION:%d,type=%s,from=%d,to=%d,amount=%s",
//                savedTx.getId(), type, fromAccountId, toAccountId, amount.toPlainString());
//        kafkaTemplate.send("transaction-events", message);
//
//        return savedTx;
//    }


    public Transaction getTransactionById(Long transactionId) {
        return repo.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public List<Transaction> getAllTransactions() {
        return repo.findAll();
    }

    public List<Transaction> getTransactionsForAccount(Long accountId) {
        return repo.findByFromAccountIdOrToAccountId(accountId, accountId);
    }

}

//package com.example.transactionservice.service;
//
//import com.example.transactionservice.client.AccountClient;
//import com.example.transactionservice.dto.AccountDto;
//import com.example.transactionservice.entity.Transaction;
//import com.example.transactionservice.repository.TransactionRepository;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//public class TransactionService {
//
//    private final TransactionRepository repo;
//    private final AccountClient accountClient;
//
//    public TransactionService(TransactionRepository repo, AccountClient accountClient) {
//        this.repo = repo;
//        this.accountClient = accountClient;
//    }
//
//    public Transaction performTransaction(Long fromAccountId, Long toAccountId, BigDecimal amount, String type) {
//        if (type.equalsIgnoreCase("TRANSFER")) {
//            // check balance first
//            AccountDto fromAcc = accountClient.getAccountById(fromAccountId);
//            if (fromAcc.getBalance().compareTo(amount) < 0) {
//                throw new RuntimeException("Insufficient balance for transfer");
//            }
//            accountClient.debit(fromAccountId, amount);
//            accountClient.credit(toAccountId, amount);
//        } else if (type.equalsIgnoreCase("DEBIT")) {
//            AccountDto fromAcc = accountClient.getAccountById(fromAccountId);
//            if (fromAcc.getBalance().compareTo(amount) < 0) {
//                throw new RuntimeException("Insufficient balance for debit");
//            }
//            accountClient.debit(fromAccountId, amount);
//        } else if (type.equalsIgnoreCase("CREDIT")) {
//            accountClient.credit(toAccountId, amount);
//        }
//
//        Transaction tx = new Transaction();
//        tx.setFromAccountId(fromAccountId);
//        tx.setToAccountId(toAccountId);
//        tx.setAmount(amount);
//        tx.setType(type);
//        tx.setTimestamp(LocalDateTime.now());
//
//        return repo.save(tx);
//    }
//
//    public List<Transaction> getAllTransactions() {
//        return repo.findAll();
//    }
//
//    public List<Transaction> getTransactionsForAccount(Long accountId) {
//        return repo.findByFromAccountIdOrToAccountId(accountId, accountId);
//    }
//
//
//}
//
////package com.example.transactionservice.service;
////
////import com.example.transactionservice.client.AccountClient;
////import com.example.transactionservice.dto.AccountDto;
////import com.example.transactionservice.entity.Transaction;
////import com.example.transactionservice.repository.TransactionRepository;
////import org.springframework.stereotype.Service;
////
////import java.math.BigDecimal;
////import java.time.LocalDateTime;
////import java.util.List;
////
////@Service
////public class TransactionService {
////
////    private final TransactionRepository repo;
////    private final AccountClient accountClient;
////
////    public TransactionService(TransactionRepository repo, AccountClient accountClient) {
////        this.repo = repo;
////        this.accountClient = accountClient;
////    }
////
////    public Transaction performTransaction(Long fromAccountId, Long toAccountId, BigDecimal amount, String type) {
////        if (type.equalsIgnoreCase("TRANSFER")) {
////            // check balance first
////            AccountDto fromAcc = accountClient.getAccountById(fromAccountId);
////            if (fromAcc.getBalance().compareTo(amount) < 0) {
////                throw new RuntimeException("Insufficient balance for transfer");
////            }
////            accountClient.debit(fromAccountId, amount);
////            accountClient.credit(toAccountId, amount);
////        } else if (type.equalsIgnoreCase("DEBIT")) {
////            AccountDto fromAcc = accountClient.getAccountById(fromAccountId);
////            if (fromAcc.getBalance().compareTo(amount) < 0) {
////                throw new RuntimeException("Insufficient balance for debit");
////            }
////            accountClient.debit(fromAccountId, amount);
////        } else if (type.equalsIgnoreCase("CREDIT")) {
////            accountClient.credit(toAccountId, amount);
////        }
////
////        Transaction tx = new Transaction();
////        tx.setFromAccountId(fromAccountId);
////        tx.setToAccountId(toAccountId);
////        tx.setAmount(amount);
////        tx.setType(type);
////        tx.setTimestamp(LocalDateTime.now());
////
////        return repo.save(tx);
////    }
////
////    public List<Transaction> getAllTransactions() {
////        return repo.findAll();
////    }
////
////    public List<Transaction> getTransactionsForAccount(Long accountId) {
////        return repo.findByFromAccountIdOrToAccountId(accountId, accountId);
////    }
////}
////
////
//////package com.example.transactionservice.service;
//////
//////import com.example.transactionservice.client.AccountClient;
//////import com.example.transactionservice.dto.TransactionRequest;
//////import com.example.transactionservice.entity.Transaction;
//////import com.example.transactionservice.repository.TransactionRepository;
//////import lombok.RequiredArgsConstructor;
//////import org.springframework.stereotype.Service;
//////
//////import java.time.LocalDateTime;
//////import java.util.Map;
//////
//////@Service
//////@RequiredArgsConstructor
//////public class TransactionService {
//////
//////    private final TransactionRepository transactionRepo;
//////    private final AccountClient accountClient;
//////
//////    public Transaction transfer(TransactionRequest request) {
//////        // 1. Get "from" account details from Account Service
//////        Map<String, Object> fromAccount = accountClient.getAccountById(request.getFromAccountId());
//////        Double currentBalance = Double.valueOf(fromAccount.get("balance").toString());
//////
//////        // 2. Check sufficient balance
//////        if (currentBalance < request.getAmount()) {
//////            throw new RuntimeException("Insufficient balance");
//////        }
//////
//////        // 3. Perform debit and credit
//////        accountClient.debitAccount(request.getFromAccountId(), request.getAmount());
//////        accountClient.creditAccount(request.getToAccountId(), request.getAmount());
//////
//////        // 4. Save transaction record
//////        Transaction tx = Transaction.builder()
//////                .fromAccountId(request.getFromAccountId())
//////                .toAccountId(request.getToAccountId())
//////                .amount(request.getAmount())
//////                .type(request.getType())
//////                .timestamp(LocalDateTime.now())
//////                .build();
//////
//////        return transactionRepo.save(tx);
//////    }
//////}
//////
//////
////////package com.example.transactionservice.service;
////////
////////import com.example.transactionservice.entity.Transaction;
////////import com.example.transactionservice.repository.TransactionRepository;
////////import org.springframework.stereotype.Service;
////////
////////import java.util.List;
////////
////////@Service
////////public class TransactionService {
////////
////////    private final TransactionRepository repo;
////////
////////    public TransactionService(TransactionRepository repo) {
////////        this.repo = repo;
////////    }
////////
////////    public Transaction createTransaction(Transaction tx) {
////////        return repo.save(tx);
////////    }
////////
////////    public Transaction getTransaction(Long id) {
////////        return repo.findById(id).orElse(null);
////////    }
////////
////////    public List<Transaction> getTransactionsByAccount(Long accountId) {
////////        return repo.findByFromAccountIdOrToAccountId(accountId, accountId);
////////    }
////////
////////    public List<Transaction> getAllTransactions() {
////////        return repo.findAll();
////////    }
////////}
//////
//////
////////package com.example.transactionservice.service;
////////
////////import com.example.transactionservice.entity.Transaction;
////////import com.example.transactionservice.repository.TransactionRepository;
////////import lombok.RequiredArgsConstructor;
////////import org.springframework.kafka.core.KafkaTemplate;
////////import org.springframework.stereotype.Service;
////////import org.springframework.transaction.annotation.Transactional;
////////
////////import java.math.BigDecimal;
////////import java.time.Instant;
////////
////////@Service
////////@RequiredArgsConstructor
////////public class TransactionService {
////////    private final TransactionRepository repo;
////////    private final KafkaTemplate<String, String> kafka;
////////
////////    @Transactional
////////    public Transaction transfer(String from, String to, BigDecimal amount) {
////////        // NOTE: In a real app, you'd verify balances and update accounts atomically across services.
////////        Transaction t = Transaction.builder()
////////                .fromAccount(from).toAccount(to).amount(amount).createdAt(Instant.now()).build();
////////        t = repo.save(t);
////////        kafka.send("transaction-events", "TRANSFER:" + t.getId() + ":" + from + "->" + to + ":" + amount);
////////        return t;
////////    }
////////}
