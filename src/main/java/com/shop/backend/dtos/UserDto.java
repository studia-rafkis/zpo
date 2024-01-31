package com.shop.backend.dtos;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String login;
    private String password;
    private String token;
    private String role;
    private String country;
    private String houseNumber;
    private String phone;
    private String state;
    private String street;
    private String streetNumber;
    private String zipCode;
    private boolean oauth_provider;
    private boolean verified;
}
