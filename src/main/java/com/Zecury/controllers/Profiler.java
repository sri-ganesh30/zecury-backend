package com.Zecury.controllers;

import com.Zecury.DTOs.DataAccessDTO.*;
import com.Zecury.DTOs.DataAccessDTO.*;
import com.Zecury.Services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class Profiler {


    private final UserServices services;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/UserDB")
    public ResponseEntity<List<UserDTO>> hello(){
        return ResponseEntity.ok(services.getUsers());
    }

//    @PreAuthorize("hasAnyRole('ADMIN' , 'USER')")
    @GetMapping("/Profile")
    public ResponseEntity<Profile> profile(){
        return ResponseEntity.ok(services.getInfo());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/TransactionList")
    public ResponseEntity<List<TransactionDBResponse>> transactionList(){

        return ResponseEntity.ok(services.getTransactions());
    }

    @GetMapping("/getTransactions")
    public ResponseEntity<List<TransactionDBResponse>> getTransactions(){
        return ResponseEntity.ok(services.getUserTransactions());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/UserCount")
    public ResponseEntity<Map<String, Integer>> userCount()
    {
        return ResponseEntity.ok(services.getUserJoinedCount());
    }


    @GetMapping("/UserTransactionCount")
    public ResponseEntity<Map<String, Integer>> userTransactionCount()
    {
        return ResponseEntity.ok(services.getUserTransactionsCount());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/TransactionCount")
    public ResponseEntity<Map<String, Integer>> TransactionCount()
    {
        return ResponseEntity.ok(services.getTransactionsCount());
    }

    @PostMapping("/ChangePassword")
    public Map<String,String> ChangePassword(@RequestBody PasswordRequest request) {
        Map<String, String> map = new HashMap<>();
        map.put("Status", services.changePassword(request));
        return map;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/GetTopFiveUsers")
    public ResponseEntity<List<TopCustomerDTO>> getTopFiveCustomers()
    {
        return ResponseEntity.ok(services.getTopFiveUsers());
    }
}
