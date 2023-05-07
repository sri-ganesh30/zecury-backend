package com.example.Zecury.DTOs.DataAccessDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PasswordRequest {
    private String newPassword;
}
