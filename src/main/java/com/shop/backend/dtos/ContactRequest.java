package com.shop.backend.dtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactRequest {
    private String firstName;
    private String email;
    private String subject;
    private String contents;
    public boolean returnEmail;
}