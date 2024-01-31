package com.shop.backend.entities;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "contact")
@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idc;
    private String firstName;
    private String email;
    private String subject;
    private String contents;
    private boolean returnEmail;
}