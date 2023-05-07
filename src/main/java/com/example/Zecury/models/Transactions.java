package com.example.Zecury.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Transactions")
public class Transactions {

    @Id
    private String transactionID= UUID.randomUUID().toString();
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payer", nullable = false)
    private User user;

    private String payeeId;
    private Double amount;
    private String message;
    private String status;
    private Date dateCreated = new Date();
    private Double instanceBalance;


}