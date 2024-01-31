package com.shop.backend.dtos;

import com.shop.backend.entities.Comments;
import com.shop.backend.entities.Product;
import com.shop.backend.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequest {
    private Long idu;
    private Long idp;
    private String text;
    private Long likes;
    private boolean deleted;
    private boolean active;
    private String data;
    private Long rating;
}