package com.shop.backend.entities;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "newsletter")
@Data
public class Newsletter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idn;
    private String email;
    private boolean isActive;
}