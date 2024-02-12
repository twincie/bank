package com.example.bank.controllers;

import com.example.bank.entity.Users;
import com.example.bank.entity.Wallet;
import com.example.bank.service.UsersService;
import com.example.bank.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UsersControllers {
    @Autowired
    private UsersService usersService;
    @Autowired
    private WalletService walletService;

    @PostMapping
    public Users createUser(@RequestBody Users users){
        return usersService.create(users);
    }
    @GetMapping("/{id}")
    public Optional<Users> readOneUser(@PathVariable Long id){
        return usersService.readOne(id);
    }

//    @GetMapping("/{id}/{walletId}")
//    public Optional<Wallet> readUserWallet(@PathVariable Long id, @PathVariable Long walletId){
//        user = usersService.readOne(id);
//        return walletService.readOne(walletId);
//    }
    @GetMapping
    public List<Users> readAllUsers(){
        return usersService.readAll();
    }
    @PutMapping("/{id}")
    public Users updateUser(@PathVariable Long id, @RequestBody Users users){
        return usersService.update(id, users);
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        usersService.delete(id);
    }

}
