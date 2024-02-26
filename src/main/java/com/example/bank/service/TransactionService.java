package com.example.bank.service;

import com.example.bank.entity.Transaction;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    public ResponseEntity<Optional<Transaction>> readOne(Long walletId, Long transactionId){
        if(walletRepository.existsById(walletId) && transactionRepository.existsById(transactionId)){
            return new ResponseEntity<>(transactionRepository.findById(transactionId), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public  List<Transaction> readOneUserTransactions(Long walletId){
        return transactionRepository.findByWalletId(walletId);
    }
//    public List<Transaction> readAll(){
//        return transactionRepository.findAll();
//    }
    public Transaction update(Long walletId, Long transactionId, Transaction updater){
        if(walletRepository.existsById(walletId) && transactionRepository.existsById(transactionId)){
            updater.setId(transactionId);
            return transactionRepository.save(updater);
        }
        return null;
    }
    public void delete(Long walletId, Long transactionId){
        if(walletRepository.existsById(walletId) && transactionRepository.existsById(transactionId)) {
            transactionRepository.deleteById(transactionId);
        }
    }
}
