package com.Zecury.DTOs.DataAccessDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TopCustomerDTO
{
    private String name;
    private String uid;
    private String accNo;
    private Double balance;
    private Integer count;
}
