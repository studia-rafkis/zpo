package com.shop.backend.entities.payu;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AuthResponse {
    private String access_token;
    private String token_type;
    private int expires_in;
    private String grant_type;
}