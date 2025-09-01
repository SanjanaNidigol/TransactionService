package com.example.transactionservice.client;

import com.example.transactionservice.dto.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(name = "account-service", url = "http://localhost:8082/accounts")
public interface AccountClient {

    @GetMapping("/{id}")
    AccountDto getAccountById(@PathVariable("id") Long id);

    @PutMapping("/{id}/debit")
    void debit(@PathVariable("id") Long id, @RequestParam("amount") BigDecimal amount);

    @PutMapping("/{id}/credit")
    void credit(@PathVariable("id") Long id, @RequestParam("amount") BigDecimal amount);
}

//package com.example.transactionservice.client;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@FeignClient(name = "account-service")
////@FeignClient(name = "account-service", url = "http://localhost:8082/accounts")
//public interface AccountClient {
//
//    @GetMapping("/{id}")
//    Map<String, Object> getAccountById(@PathVariable("id") Long id);
//
//    @PutMapping("/{id}/debit")
//    void debitAccount(@PathVariable("id") Long id, @RequestParam("amount") Double amount);
//
//    @PutMapping("/{id}/credit")
//    void creditAccount(@PathVariable("id") Long id, @RequestParam("amount") Double amount);
//}
