package com.example.Zecury.Services;

import com.example.Zecury.DTOs.DataAccessDTO.TopCustomerDTO;
import com.example.Zecury.DTOs.DataAccessDTO.PasswordRequest;
import com.example.Zecury.DTOs.DataAccessDTO.Profile;
import com.example.Zecury.DTOs.DataAccessDTO.TransactionDBResponse;
import com.example.Zecury.DTOs.DataAccessDTO.UserDTO;
import com.example.Zecury.models.Transactions;
import com.example.Zecury.models.User;
import com.example.Zecury.repos.TransactionsRepository;
import com.example.Zecury.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserServices {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String dateString = dateFormat.format(new Date());



    private final UserRepository userRepo;
    private final TransactionsRepository transactionsRepository;
    public User convertDTOToUser(UserDTO userDTO){
//        Date dateOfBirth = new Date(2003,02,31);
        Date dateCreated = new Date();
        Date lastLoggedin = new Date();
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setAddress(userDTO.getAddress());
        user.setMobile(userDTO.getMobile());
        user.setAadarno(userDTO.getAadarno());
        user.setPanno(userDTO.getPanno());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setDateCreated(dateCreated);
        user.setLastLoggedin(lastLoggedin);
        return user;
    }

    public UserDTO convertUserToDTO(User user){
        UserDTO userDto = new UserDTO();
        userDto.setName(user.getName());
        userDto.setPassword("[PROTECTED]");
        userDto.setEmail(user.getEmail());
        userDto.setAddress(user.getAddress());
        userDto.setMobile(user.getMobile());
        userDto.setAadarno(user.getAadarno());
        userDto.setPanno(user.getPanno());
        userDto.setDateOfBirth(user.getDateOfBirth());
        userDto.setUid(user.getUId());
        userDto.setAccNo(user.getAccNo());
//        userDto.setTransactions(user.getTransactions());
        return userDto;
    }
    public List<UserDTO> getUsers(){
        List<User> users = userRepo.findAll();
        return users.stream().map(this::convertUserToDTO).toList();
    }

    public Profile getInfo(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userRepo.findUserByuId(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Profile profile=new Profile();
        profile.setAccNo(user.getAccNo());
        profile.setName(user.getName());
        profile.setName(user.getName());
        profile.setUid(user.getUId());
        profile.setBalance(user.getBalance());
        profile.setLastlogin(user.getLastLoggedin());

        profile.setCount(transactionsRepository.findAllByUser(user).toArray().length);
        return profile;
    }

    public TransactionDBResponse convertTransactionsToDTO(Transactions transactions){
        TransactionDBResponse transactionDBResponse = new TransactionDBResponse();
        transactionDBResponse.setAmount(transactions.getAmount());
        transactionDBResponse.setMessage(transactions.getMessage());
        transactionDBResponse.setTId(transactions.getTransactionID());
        transactionDBResponse.setCreateddate(transactions.getDateCreated());
        transactionDBResponse.setStatus(transactions.getStatus());
        transactionDBResponse.setBalance(transactions.getInstanceBalance());
        User user = transactions.getUser();
        transactionDBResponse.setPayerId(user.getUId());

        return transactionDBResponse;
    }

    public List<TransactionDBResponse> getTransactions(){
        List<Transactions> transactions = transactionsRepository.findAll();
        return transactions.stream().map(this::convertTransactionsToDTO).toList();
    }


    public List<TransactionDBResponse> getUserTransactions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userRepo.findUserByuId(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return transactionsRepository.findAllByUser(user).stream().map(this::convertTransactionsToDTO).toList();
    }

    public Map<String, Integer> getUserJoinedCount(){

        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        var cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH,-6);

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.add(Calendar.DAY_OF_MONTH,-5);
        Map<String, Integer> usersCount = new LinkedHashMap<>();

        for (int i = 0; i < 7; i++) {
            int count = userRepo.findAllByDateCreatedBetween(cal.getTime(), cal1.getTime()).size();
            usersCount.put(getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK)), count);
            cal1.add(Calendar.DAY_OF_MONTH, 1);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return usersCount;
    }

    public Map<String, Integer> getUserTransactionsCount(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userRepo.findUserByuId(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        var cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH,-6);

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.add(Calendar.DAY_OF_MONTH,-5);
        Map<String, Integer> transactionsCount = new LinkedHashMap<>();

        for (int i = 0; i < 7; i++) {
            int count = transactionsRepository.findAllByUserAndDateCreatedBetween(user, cal.getTime(), cal1.getTime()).size();
            transactionsCount.put(getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK)), count);
            cal1.add(Calendar.DAY_OF_MONTH, 1);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return transactionsCount;
    }

    private static String getDayOfWeek(int dayOfWeek) {
        return switch (dayOfWeek) {
            case 1 -> "Sunday";
            case 2 -> "Monday";
            case 3 -> "Tuesday";
            case 4 -> "Wednesday";
            case 5 -> "Thursday";
            case 6 -> "Friday";
            case 7 -> "Saturday";
            default -> "";
        };
    }

    public Map<String,Integer> getTransactionsCount(){
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        var cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH,-6);

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.add(Calendar.DAY_OF_MONTH,-5);
        Map<String, Integer> transactionsCount = new LinkedHashMap<>();

        for (int i = 0; i < 7; i++) {
            int count = transactionsRepository.findAllByDateCreatedBetween(cal.getTime(), cal1.getTime()).size();
            transactionsCount.put(getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK)), count);
            cal1.add(Calendar.DAY_OF_MONTH, 1);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return transactionsCount;
    }

    public String changePassword(PasswordRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userRepo.findUserByuId(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(user);
        return "Password changed";
    }

    public List<TopCustomerDTO> getTopFiveUsers(){
        List<User> users = userRepo.findTop5ByOrderByBalanceDesc();
        return users.stream().map(this::convertUserToTopCustomerDTO).toList();
    }

    public TopCustomerDTO convertUserToTopCustomerDTO(User user){
        TopCustomerDTO topCustomerDTO=new TopCustomerDTO();
        topCustomerDTO.setAccNo(user.getAccNo());
        topCustomerDTO.setName(user.getName());
        topCustomerDTO.setName(user.getName());
        topCustomerDTO.setUid(user.getUId());
        topCustomerDTO.setBalance(user.getBalance());
        topCustomerDTO.setCount(transactionsRepository.findAllByUser(user).toArray().length);
        return topCustomerDTO;
    }

}
