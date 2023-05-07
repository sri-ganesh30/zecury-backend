package com.example.Zecury.bootstrap;

import com.example.Zecury.models.Role;
import com.example.Zecury.models.User;
import com.example.Zecury.repos.UserRepository;
import com.example.Zecury.repos.RoleRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.*;

@RequiredArgsConstructor
@Component
public class Bootstrap implements CommandLineRunner {


    private final UserRepository userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

//implementing the role table
        Role user_role = new Role(1L, "ROLE_USER");
        Role admin = new Role (2L, "ROLE_ADMIN");

//Code for creating an admin, comment when u don't want an admin to be formed while run
        roleRepo.saveAll(List.of(user_role, admin));
//    Role admin = roleRepo.findByName("ROLE_ADMIN");
        User user = new User("admin",  passwordEncoder.encode("admin@123"),  "address",  "email",  "mobile", new Date(), new Date(), new Date(),  "aadarno",  "panno", Set.of(admin));
        userRepo.save(user);


    }
}
