package com.example.bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String accountNumber = accountNumberGenerator().toString();
    private BigDecimal amount = BigDecimal.ZERO;

//    @OneToMany(mappedBy = "wallet")
//    private List<Transaction> transactions;

    public Long accountNumberGenerator(){
        Random random = new Random();
        int randomNumber = 1000000000 + random.nextInt(900000000);
        return (long) randomNumber;
    }
}
