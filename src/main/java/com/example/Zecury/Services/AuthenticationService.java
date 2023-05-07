package com.example.Zecury.Services;

import com.example.Zecury.DTOs.*;
import com.example.Zecury.DTOs.DataAccessDTO.UserDTO;
import com.example.Zecury.models.Role;
import com.example.Zecury.models.User;
import com.example.Zecury.repos.RoleRepo;
import com.example.Zecury.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.Zecury.EmailSender.LoginEmailSenderService;


import java.util.Date;
import java.util.Iterator;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepo;
    private final JWTService jwtService;
    private final UserServices userServices;
    private final AuthenticationManager authenticationManager;
    private final RoleRepo roleRepo;

    @Autowired
    private LoginEmailSenderService senderService;

    public String register(RegisterRequest request) {
        String password= RandomStringUtils.randomAlphanumeric(8)+"@bankease";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserDTO userDTO =new UserDTO();
        userDTO.setName(request.getName());
        userDTO.setAddress(request.getAddress());
        String userPassword = password;
        userDTO.setPassword(passwordEncoder.encode(password));
        userDTO.setEmail(request.getEmail());
        userDTO.setMobile(request.getMobile());
        userDTO.setAadarno(request.getAadarno());
        userDTO.setDateOfBirth(request.getDateOfBirth());
        userDTO.setPanno(request.getPanno());
        Role role = roleRepo.findByName("ROLE_USER");
        User user = userServices.convertDTOToUser(userDTO);
        user.setRoles(Set.of(role));
        userRepo.save(user);
        senderService.sendEmail(user.getEmail(),user.getUId(),user.getName(),userPassword);
        return "Registration Successful";

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUId(),request.getPassword()
                )
        );

        var user =  userRepo.findUserByuId(request.getUId()).orElseThrow(()->new UsernameNotFoundException("user not exist"));

//        var user =  userRepo.findUserByuId(request.getUId());
        user.setLastLoggedin(new Date());
        var jwtToken = jwtService.generateToken(user);
        Iterator<Role> iterator = user.getRoles().iterator();
        Role firstElement = iterator.next();

        String role = firstElement.getName();
        return AuthenticationResponse.builder().token(jwtToken).role(role).build();

    }

    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "Logout";
    }
}
