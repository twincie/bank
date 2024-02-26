package com.example.bank.controllers;

import com.example.bank.dto.TransferRequest;
import com.example.bank.entity.Transaction;
import com.example.bank.entity.Users;
import com.example.bank.service.UsersService;
import com.example.bank.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UsersControllers {
    @Autowired
    private UsersService usersService;
    @Autowired
    private WalletService walletService;

    // only for admin
    @GetMapping
    public List<Users> readAllUsers(){
        return usersService.readAll();
    }

    @PostMapping("/create")
    public Users createUser(@RequestBody Users users){
        return usersService.create(users);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Users> readOneUser(@PathVariable Long id){
        return usersService.readOne(id);
    }

    @PutMapping("/{id}")
    public Users updateUser(@PathVariable Long id, @RequestBody Users users){
        return usersService.update(id, users);
    }

    //for admin
    @DeleteMapping("{id}/delete")
    public void deleteUser(@PathVariable Long id){
        usersService.delete(id);
    }
//
//    @PostMapping("/{id}/topup")
//    public void topup(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request){
//        BigDecimal amount = request.get("amount");
//        usersService.userTopup(id, amount);
//    }
//
//    @PostMapping("/{id}/withdraw")
//    public void withdraw(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request){
//        BigDecimal amount = request.get("amount");
//        usersService.userWithdraw(id, amount);
//    }
//
//    @PostMapping("/{id}/transfer")
//    public void transfer(@PathVariable Long id, @RequestBody Map<String, Object> request){
//        Object amountObj = request.get("amount");
//        Object recipientNumObj = request.get("recipientNumber");
//        BigDecimal amount = new BigDecimal(amountObj.toString());
//        String recipientNumber = recipientNumObj.toString();
//
//        TransferRequest transferRequest = new TransferRequest();
//        transferRequest.setRecipientNumber(recipientNumber);
//        transferRequest.setAmount(amount);
//
//        usersService.userTransfer(id, transferRequest);
//    }
//
//    @GetMapping("/{id}/transaction")
//    public List<Transaction> getUsersTransactions(@PathVariable Long id){
//        return usersService.getUsersTransactions(id);
//    }

}
