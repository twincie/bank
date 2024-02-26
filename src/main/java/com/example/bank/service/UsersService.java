package com.example.bank.service;


import com.example.bank.dto.TransferRequest;
import com.example.bank.entity.Transaction;
import com.example.bank.entity.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UsersService {
    Users create(Users user);
    ResponseEntity<Users> readOne(Long id);
    List<Users> readAll();
    public Users update(Long id, Users updater);
    void delete(Long id);
//    Optional<Users> userTopup(Long id, BigDecimal amount);
//    void userWithdraw(Long userId, BigDecimal amount);
//    void userTransfer(Long id, TransferRequest transferRequest);

//    List<Transaction> getUsersTransactions(Long id);

    UserDetailsService userDetailsService();

}
