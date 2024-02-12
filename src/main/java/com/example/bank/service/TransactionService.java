package com.example.bank.service;

import com.example.bank.entity.Transaction;
import com.example.bank.entity.TransactionType;
import com.example.bank.entity.Wallet;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletRepository walletRepository;
    // CRUD methods
    public Transaction create(Transaction transaction){
        return transactionRepository.save(transaction);
    }
    public Optional<Transaction> readOne(Long id){
        return transactionRepository.findById(id);
    }
    public List<Transaction> readAll(){
        return transactionRepository.findAll();
    }
    public Transaction update(Long id, Transaction updater){
        updater.setId(id);
        return transactionRepository.save(updater);
    }
    public void delete(Long id){
        transactionRepository.deleteById(id);
    }

}
