package com.example.bank.service;

import com.example.bank.entity.Users;
import com.example.bank.entity.Wallet;
import com.example.bank.repository.UsersRepository;
import com.example.bank.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private WalletRepository walletRepository;

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

//    public ResponseEntity<String> signUp(Users users){
//        if (usersRepository.findByEmail(users.getEmail()).isPresent()){
//            return ResponseEntity.badRequest().body("Email is already taken");
//        } else{
//            users.setPassword();
//        }
//    }
}
