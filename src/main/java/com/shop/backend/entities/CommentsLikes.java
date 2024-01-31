package com.shop.backend.entities;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "comments_likes")
@Data
public class CommentsLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcl;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idcm")
    private Comments comments;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idu")
    private User user;
    private boolean active;
}