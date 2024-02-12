package com.example.bank.controllers;

import com.example.bank.entity.Wallet;
import com.example.bank.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @PostMapping
    public Wallet createWallet(@RequestBody Wallet wallet){
        return walletService.create(wallet);
    }
    @GetMapping("/{id}")
    public Optional<Wallet> readOneWallet(@PathVariable Long id){
        return walletService.readOne(id);
    }
    @GetMapping
    public List<Wallet> readAllWallets(){
        return walletService.readAll();
    }
    @PutMapping("/{id}")
    public Wallet updateWallet(@PathVariable Long id, @RequestBody Wallet wallet){
        return walletService.update(id, wallet);
    }
    @DeleteMapping("/{id}")
    public void deleteWallet(@PathVariable Long id){
        walletService.delete(id);
    }

    @PutMapping("/{id}/topup")
    public void topUpWallet(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request){
        BigDecimal amount = request.get("amount");
        walletService.topup(id, amount);
    }

    @PutMapping("/{id}/withdraw")
    public void withdrawWallet(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request){
        BigDecimal amount = request.get("amount");
        walletService.withdraw(id, amount);
    }

    @PutMapping("/{id}/transfer")
    public void transferWallet(@PathVariable Long id, @RequestBody Map<String, Object> request){
        Object amountObj = request.get("amount");
        Object receiverAccountNumberObj = request.get("accountNumber");
        BigDecimal amount = new BigDecimal(amountObj.toString());
        String receiverAccountNumber = receiverAccountNumberObj.toString();

//        System.out.println(amountObj);
//        System.out.println(receiverAccountNumberObj);
        walletService.transfer(id, amount, receiverAccountNumber);
    }

}
