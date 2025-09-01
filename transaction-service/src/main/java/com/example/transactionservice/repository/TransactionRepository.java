package com.example.transactionservice.repository;

import com.example.transactionservice.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccountIdOrToAccountId(Long fromAccountId, Long toAccountId);
}


//package com.example.transactionservice.repository;
//
//import com.example.transactionservice.entity.Transaction;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface TransactionRepository extends JpaRepository<Transaction, Long> {
//}


//package com.example.transactionservice.repository;
//
//import com.example.transactionservice.entity.Transaction;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface TransactionRepository extends JpaRepository<Transaction, Long> {
//    List<Transaction> findByFromAccountIdOrToAccountId(Long fromAccountId, Long toAccountId);
//}


//package com.example.transactionservice.repository;
//
//import com.example.transactionservice.entity.Transaction;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface TransactionRepository extends JpaRepository<Transaction, Long> { }
