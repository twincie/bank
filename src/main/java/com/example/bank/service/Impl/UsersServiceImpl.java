package com.example.bank.service.Impl;

import com.example.bank.dto.TransferRequest;
import com.example.bank.entity.Role;
import com.example.bank.entity.Transaction;
import com.example.bank.entity.Users;
import com.example.bank.entity.Wallet;
import com.example.bank.repository.UsersRepository;
import com.example.bank.repository.WalletRepository;
import com.example.bank.service.TransactionService;
import com.example.bank.service.UsersService;
import com.example.bank.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
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

    @Autowired
    private JWTServiceImpl jwtService;


    //    =================================== CRUD START =================================
    public Users create(Users user){
        Wallet wallet = new Wallet();
        user.setWallet(wallet);
        walletRepository.save(wallet);
        return usersRepository.save(user);
    }

    public ResponseEntity<Users> readOne(Long id){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println(currentUser);
        Optional<Users> usersOptional = usersRepository.findById(id);
        if (usersOptional.isPresent() || isAdmin()){
            Users users = usersOptional.get();
            if (!users.getUsername().equals(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok(users);
        }
        return ResponseEntity.notFound().build();
    }

//    public Optional<Users> readOne(Long id, HttpServletRequest request){
//        String authorizationHeader = request.getHeader("Authorization");
//        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")){
//            String token = authorizationHeader.substring(7);
//            System.out.println(token);
//            String userName = jwtService.extractUserName(token);
//            System.out.println( "Username : " + userName);
//        }
//        return usersRepository.findById(id);
//    }
    public List<Users> readAll(){
        return usersRepository.findAll();
    }
    public Users update(Long id, Users updater){
        Users existingUser = usersRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("User Not Found"));

        if(usersRepository.existsById(id)){
            existingUser.setEmail(updater.getEmail());
            existingUser.setUsername(updater.getUsername());
            return usersRepository.save(existingUser);
        } else
            return null;
    }
    public void delete(Long id){
        usersRepository.deleteById(id);
    }

    //=================================== CRUD END =================================

//    //=================================== PERFORMING TRANSACTIONS START =================================
//    public Optional<Users> userTopup(Long id, BigDecimal amount){
//        Users users = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
//        walletService.topup(users.getWallet().getId(), amount);
//        return usersRepository.findById(id);
//    }
//    public void userWithdraw(Long userId, BigDecimal amount) {
//        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//        walletService.withdraw(user.getWallet().getId(), amount);
//    }
//    public void userTransfer(Long id, TransferRequest transferRequest) {
//        Users user = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("Sender User not found"));
//        Wallet recipient = walletRepository.findByAccountNumber(transferRequest.getRecipientNumber()).orElseThrow(() -> new RuntimeException("Receiver wallet not found"));
//
//        walletService.transfer(user.getWallet().getId(), transferRequest.getAmount() , recipient.getAccountNumber());
//    }

    //=================================== PERFORMING TRANSACTIONS END =================================

//    public List<Transaction> getUsersTransactions(Long id){
//        Users user = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
//        Long walletId = user.getWallet().getId();
//        return transactionService.readOneUserTransactions(walletId);
//    }

    public boolean isAdmin(){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> users = usersRepository.findByUsername(currentUser);
        if (users.isPresent()){
            Users user = users.get();
            return user.getRole() == Role.ADMIN;
        }
        return false;
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
