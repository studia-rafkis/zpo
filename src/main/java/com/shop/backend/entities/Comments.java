package com.shop.backend.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "comments")
@Data
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcm;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idu")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idp")
    private Product product;
    private String data;
    private Long likes;
    private String text;
    private boolean deleted;
    private boolean active;
    private Long rating;
}