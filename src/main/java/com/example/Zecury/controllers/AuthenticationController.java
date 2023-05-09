package com.example.Zecury.controllers;

import com.example.Zecury.DTOs.AuthenticationRequest;
import com.example.Zecury.DTOs.AuthenticationResponse;
import com.example.Zecury.Services.AuthenticationService;
import com.example.Zecury.DTOs.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @GetMapping("/")
    public String test()
    {
        return "Success";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request)
    {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request)
    {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(){
        return  ResponseEntity.ok(service.logout());
    }



}
