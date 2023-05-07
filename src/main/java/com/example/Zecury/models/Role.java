package com.example.Zecury.models;


import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "_role")
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
