package com.example.Zecury.controllers;

import com.example.Zecury.DTOs.Transactions.*;
import com.example.Zecury.Services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PaymentController {

    private final PaymentService service;

    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> pay(@RequestBody @Valid PaymentRequest request)
    {
        return ResponseEntity.ok(service.pay(request));
    }

    @PostMapping("/deposit")
    public ResponseEntity<DepositResponse> deposit(@RequestBody @Valid DepositRequest request)
    {
        return ResponseEntity.ok(service.deposit(request));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<WithdrawResponse> withdraw(@RequestBody @Valid WithdrawRequest request)
    {
        return ResponseEntity.ok(service.withdraw(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/Profit")
    public ResponseEntity<Map<String, Double>> userCount()
    {
        return ResponseEntity.ok(service.getProfitCount());
    }


}
