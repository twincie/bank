package com.example.bank.service.Impl;

import com.example.bank.dto.TransferRequest;
import com.example.bank.entity.Transaction;
import com.example.bank.entity.Users;
import com.example.bank.entity.Wallet;
import com.example.bank.repository.UsersRepository;
import com.example.bank.repository.WalletRepository;
import com.example.bank.service.TransactionService;
import com.example.bank.service.UsersService;
import com.example.bank.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletService walletService;

    // CRUD
    public Users create(Users user){
        Wallet wallet = new Wallet();
        user.setWallet(wallet);
        walletRepository.save(wallet);
        return usersRepository.save(user);
    }
    public Optional<Users> readOne(Long id){
        return usersRepository.findById(id);
    }
    public List<Users> readAll(){
        return usersRepository.findAll();
    }
    public Users update(Long id, Users updater){
        updater.setId(id);
        return usersRepository.save(updater);
    }
    public void delete(Long id){
        usersRepository.deleteById(id);
    }

    // performing Transactions
    public Optional<Users> userTopup(Long id, BigDecimal amount){
        Users users = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        walletService.topup(users.getWallet().getId(), amount);
        return usersRepository.findById(id);
    }
    public void userWithdraw(Long userId, BigDecimal amount) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        walletService.withdraw(user.getWallet().getId(), amount);
    }
    public void userTransfer(Long id, TransferRequest transferRequest) {
        Users user = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("Sender User not found"));
        Wallet recipient = walletRepository.findByAccountNumber(transferRequest.getRecipientNumber()).orElseThrow(() -> new RuntimeException("Receiver wallet not found"));

        walletService.transfer(user.getWallet().getId(), transferRequest.getAmount() , recipient.getAccountNumber());
    }

    public List<Transaction> getUsersTransactions(Long id){
        Users user = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("Sender User not found"));
        Long walletId = user.getWallet().getId();
        return transactionService.readOneUserTransactions(walletId);
    }

    @Override
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return usersRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }
}
