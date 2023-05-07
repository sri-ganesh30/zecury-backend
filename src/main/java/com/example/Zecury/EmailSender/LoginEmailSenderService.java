package com.example.Zecury.EmailSender;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class LoginEmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail,String id,String username,String password){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("birthdaywisher67@gmail.com");
        message.setTo(toEmail);
        String body="Dear "+username+"\n" +
                "\n" +
                "    We are thrilled to welcome you as a new customer of our bank. We are dedicated to providing you with the best banking experience possible and are looking forward to serving all of your financial needs.\n" +
                "\n" +
                "    As a new customer, you will have access to a wide range of products and services, including checking and savings accounts, loans, credit cards, and online banking. To get started, we recommend that you visit our website and explore all of the options available to you.\n" +
                "\n" +
                "    Here is your customer_id : "+ id +"\n" +
                "    Here is your password : "+ password +"\n" +
                "\n" +
                "    To ensure the security of your account, please be sure to keep your account information and login credentials private. If you ever have any questions or concerns, please don't hesitate to contact us.\n" +
                "\n" +
                "    Again, welcome to our bank and thank you for choosing us as your financial partner.";
        String subject="Registering for BankEase Banking";
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);

    }
}
