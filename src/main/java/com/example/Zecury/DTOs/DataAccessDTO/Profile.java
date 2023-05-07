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
public class Profile {
    private String name;

    private String uid;
    private String accNo;

    private Double balance;

    private Date lastlogin;
    private Integer count;
}
