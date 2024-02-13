package com.example.bank;

import com.example.bank.entity.Role;
import com.example.bank.entity.Users;
import com.example.bank.entity.Wallet;
import com.example.bank.repository.UsersRepository;
import com.example.bank.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;

@SpringBootApplication
public class BankApplication implements CommandLineRunner {

	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private WalletRepository walletRepository;

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Users adminAccount = usersRepository.findByRole(Role.ADMIN);
		if(null == adminAccount){
			Wallet wallet = new Wallet();
			wallet.setAccountNumber("1000000000");
			wallet.setAmount(BigDecimal.ZERO);

			Users users = new Users();

			users.setUsername("globalbank");
			users.setEmail("globalbank@gmail.com");
			users.setPassword(new BCryptPasswordEncoder().encode("globalbank"));
			users.setRole(Role.ADMIN);
			users.setWallet(wallet);
			walletRepository.save(wallet);
			usersRepository.save(users);
		}
	}
}
