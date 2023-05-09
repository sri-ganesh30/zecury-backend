package com.Zecury.Services;

import com.Zecury.DTOs.Transactions.*;
import com.Zecury.ExceptionsforThisAppliccation.InsufficiantAmountException;
import com.Zecury.models.Transactions;
import com.Zecury.models.User;
import com.Zecury.repos.TransactionsRepository;
import com.Zecury.repos.UserRepository;
import com.Zecury.DTOs.Transactions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String dateString = dateFormat.format(new Date());

    private final UserRepository userRepo;
    private final TransactionsRepository transactionsRepository;
    public PaymentResponse pay(PaymentRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userRepo.findUserByuId(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User payeeUser = userRepo.findUserByAccNo(request.getPayeeId()).orElseThrow(() -> new UsernameNotFoundException("payee not found"));
        amountValidation(user, request.getAmount());

        if(transactionsRepository.findAllByUser(user).stream().count()%10==0){
            serviceCharges(user, 10.0, "Maintenance Fee");

        }
        if(request.getAmount()>=5000){
            serviceCharges(user, 10.0, "Operational Fee");

        }
        payeeUser.setBalance(payeeUser.getBalance() + request.getAmount());
        user.setBalance(user.getBalance() - request.getAmount());
        Transactions transaction = new Transactions();
        transaction.setPayeeId(request.getPayeeId());
        transaction.setAmount(request.getAmount());
        transaction.setUser(user);
        transaction.setMessage("Rs "+request.getAmount()+" is sent to "+payeeUser.getName());
        transaction.setStatus("SUCCESS");
        transaction.setInstanceBalance(user.getBalance());

        transactionsRepository.save(transaction);


        Transactions transactions1 = new Transactions();
        transactions1.setPayeeId(user.getUId());
        transactions1.setAmount(request.getAmount());
        transactions1.setUser(payeeUser);
        transactions1.setMessage("Rs "+ request.getAmount()+" is received from "+user.getName());
        transactions1.setStatus("SUCCESS");
        transactions1.setInstanceBalance(payeeUser.getBalance());
        transactionsRepository.save(transactions1);


        userRepo.saveAll(List.of(payeeUser, user));
        return new PaymentResponse("success, the amount is sent to "+payeeUser.getName(), request.getAmount());
    }

    private void serviceCharges(User user,double amount,String message) {
        user.setBalance(user.getBalance()-10);
        userRepo.save(user);
        Transactions transactions=new Transactions();
        transactions.setPayeeId(user.getUId());
        transactions.setUser(user);
        transactions.setAmount(amount);
        transactions.setMessage(message);
        transactions.setStatus("SUCCESS");
        transactions.setInstanceBalance(user.getBalance());
        transactionsRepository.save(transactions);
    }

    public boolean amountValidation(User user, double amount) {
        double balance = user.getBalance();
        if(amount>balance-1000)
        {
            Transactions failedTransaction =new Transactions();
            failedTransaction.setUser(user);
            failedTransaction.setAmount(amount);
            failedTransaction.setMessage("minimum balance reached");
            failedTransaction.setStatus("FAILED");
            failedTransaction.setPayeeId(user.getUId());
            failedTransaction.setInstanceBalance(user.getBalance());
            transactionsRepository.save(failedTransaction);

            throw new InsufficiantAmountException("minimum balance reached");
        }else {
            return true;
        }
    }

    public DepositResponse deposit(DepositRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userRepo.findUserByuId(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(transactionsRepository.findAllByUser(user).stream().count()%10==0){
            serviceCharges(user, 10.0, "Maintenance Fee");

        }
        if(request.getAmount()>=5000){
            serviceCharges(user, 10.0, "Operational Fee");

        }
        user.setBalance(user.getBalance()+ request.getAmount());
        Transactions transactions =new Transactions();
        transactions.setPayeeId(user.getUId());
        transactions.setUser(user);
        transactions.setAmount(request.getAmount());
        transactions.setMessage("Rs "+request.getAmount()+" is deposited to your Account");
        transactions.setStatus("SUCCESS");
        transactions.setInstanceBalance(user.getBalance());
        transactionsRepository.save(transactions);

        userRepo.save(user);
        return new DepositResponse("success,the amount is added to your account ", request.getAmount());
    }

    public WithdrawResponse withdraw(WithdrawRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userRepo.findUserByuId(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        amountValidation(user, request.getAmount());

        if(transactionsRepository.findAllByUser(user).stream().count()%10==0){
            serviceCharges(user, 10.0, "Maintenance Fee");

        }
        if(request.getAmount()>=5000){
            serviceCharges(user, 10.0, "Operational Fee");

        }
        user.setBalance(user.getBalance()- request.getAmount());
        Transactions transactions =new Transactions();
        transactions.setPayeeId(user.getUId());
        transactions.setUser(user);
        transactions.setAmount(request.getAmount());
        transactions.setMessage("Rs "+request.getAmount()+" is withdraw from your account");
        transactions.setStatus("SUCCESS");
        transactions.setInstanceBalance(user.getBalance());
        transactionsRepository.save(transactions);

        userRepo.save(user);
        return new WithdrawResponse("success", request.getAmount());
    }

    public Map<String , Double> getProfitCount(){
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
        Map<String, Double> profits = new LinkedHashMap<>();

        for (int i = 0; i < 7; i++) {
            Double profit = transactionsRepository.findAllByMessageIsLikeAndDateCreatedBetween("%Fee",  cal.getTime(), cal1.getTime())
                    .stream()
                    .map(Transactions::getAmount)
                    .mapToDouble(Double::doubleValue).sum();
            profits.put(getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK)), profit);
            cal1.add(Calendar.DAY_OF_MONTH, 1);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return profits;
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

}
