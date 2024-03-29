package com.example.bank.controllers;

import com.example.bank.entity.Transaction;
import com.example.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/wallet/{walletId}/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction){
        return transactionService.create(transaction);
    }
    @GetMapping("/{transactionId}")
    public ResponseEntity<Optional<Transaction>> readOneTransaction(@PathVariable Long walletId, @PathVariable Long transactionId){
        return transactionService.readOne(walletId,transactionId);
    }
    @GetMapping
    public List<Transaction> readAllUserTransactions(@PathVariable Long walletId){
        return transactionService.readOneUserTransactions(walletId);
    }
    @PutMapping("/{transactionId}")
    public Transaction updateTransaction(@PathVariable Long walletId, @PathVariable Long transactionId, @RequestBody Transaction transaction){
        return transactionService.update(walletId, transactionId, transaction);
    }
    @DeleteMapping("/{transactionId}")
    public void deleteTransaction(@PathVariable Long walletId, @PathVariable Long transactionId){
        transactionService.delete(walletId, transactionId);
    }

}
