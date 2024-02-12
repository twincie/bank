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
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    public Wallet create(Wallet wallet){
        return walletRepository.save(wallet);
    }
    public Optional<Wallet> readOne(Long id){
        return walletRepository.findById(id);
    }
//    public Optional<Wallet> readOne(String accountNumber){
//        return Optional.ofNullable(walletRepository.findByAccountNumber(accountNumber));

//    }
    public List<Wallet> readAll(){
        return walletRepository.findAll();
    }
    public Wallet update(Long id, Wallet updater){
        updater.setId(id);
        return walletRepository.save(updater);
    }
    public void delete(Long id){
        walletRepository.deleteById(id);
    }

    public void topup(Long id, BigDecimal amount){
        Optional<Wallet> walletoptional = walletRepository.findById(id);
        if (walletoptional.isPresent()){
            Wallet wallet = walletoptional.get();
            if (amount.compareTo(charges(amount)) > 0){
                BigDecimal balance = wallet.getAmount().add(amount.subtract(charges(amount)));
                wallet.setAmount(balance);
                walletRepository.save(wallet);

                Transaction transaction = new Transaction();
                transaction.setAmount(amount);
                transaction.setType(TransactionType.TOPUP);
                transaction.setWallet(wallet);
                transactionRepository.save(transaction);
            }
        }
    }

    public void withdraw(Long id, BigDecimal amount){
        Optional<Wallet> walletoptional = walletRepository.findById(id);
        if (walletoptional.isPresent()){
            Wallet wallet = walletoptional.get();
            // BigDecimal balance = wallet.getAmount();
            if(amount.compareTo(BigDecimal.ZERO) >0 && wallet.getAmount().compareTo(amount.add(charges(amount))) >= 0 ){
                BigDecimal balance = wallet.getAmount().subtract(amount.add(charges(amount)));
                wallet.setAmount(balance);
                walletRepository.save(wallet);

                Transaction transaction = new Transaction();
                transaction.setAmount(amount);
                transaction.setType(TransactionType.WITHDRAW);
                transaction.setWallet(wallet);
                transactionRepository.save(transaction);
            }

        }
    }

    public void transfer(Long id, BigDecimal amount, String accountNumber){
        Optional<Wallet> senderWallet = walletRepository.findById(id);
        Optional<Wallet> receiverWallet = walletRepository.findByAccountNumber(accountNumber);
        if (senderWallet.isPresent() && receiverWallet.isPresent()){
            Wallet sendWallet = senderWallet.get();
            Wallet receiveWallet = receiverWallet.get();

            BigDecimal senderBalance = sendWallet.getAmount().subtract(amount.add(charges((amount))));
            sendWallet.setAmount(senderBalance);
            walletRepository.save(sendWallet);

            BigDecimal receiverBalance = receiveWallet.getAmount().add(amount.subtract(charges(amount)));
            receiveWallet.setAmount(receiverBalance);
            walletRepository.save(receiveWallet);

            Transaction sendTransaction = new Transaction();
            sendTransaction.setAmount(amount);
            sendTransaction.setType(TransactionType.TRANSFER);
            sendTransaction.setWallet(sendWallet);
            transactionRepository.save(sendTransaction);

            Transaction receiveTransaction = new Transaction();
            receiveTransaction.setAmount(amount);
            receiveTransaction.setType(TransactionType.TOPUP);
            receiveTransaction.setWallet(sendWallet);
            transactionRepository.save(receiveTransaction);
        }
    }
    public BigDecimal charges(BigDecimal amount){
        if (amount.compareTo(BigDecimal.valueOf(5000)) <= 0){
            return BigDecimal.valueOf(10);
        }else if (amount.compareTo(BigDecimal.valueOf(50000)) <= 0){
            return BigDecimal.valueOf(25);
        } else{
            return BigDecimal.valueOf(50);
        }
    }
}