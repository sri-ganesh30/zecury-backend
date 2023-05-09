package com.Zecury.DTOs.DataAccessDTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private String name;
    private String password;
    private String address;
    private String email;
    private String mobile;
    private Date dateOfBirth;
    private String aadarno;
    private String panno;
    private String uid;
    private String accNo;
//    private Set<Transactions> transactions;
}
