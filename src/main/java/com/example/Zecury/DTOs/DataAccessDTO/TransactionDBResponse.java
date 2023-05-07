package com.example.Zecury.DTOs.DataAccessDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionDBResponse {

    private String payerId;
    private Double amount;
    private String message;
    private String status;
    private String tId;
    private Date createddate;
    private Double balance;
}
