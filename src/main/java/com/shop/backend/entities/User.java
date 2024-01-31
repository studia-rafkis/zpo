package com.shop.backend.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    @Size(max = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Size(max = 100)
    private String lastName;

    @Column(nullable = false)
    @Size(max = 100)
    private String login;

    @Column(nullable = false)
    @Size(max = 100)
    private String password;

    @Size(max = 100)
    private String country;

    @Size(max = 100)
    private String houseNumber;

    @Size(max = 100)
    private String phone;

    @Size(max = 100)
    private String state;

    @Size(max = 100)
    private String street;

    @Size(max = 100)
    private String streetNumber;

    @Size(max = 100)
    private String zipCode;

    @Column(name = "oauth_provider")
    private boolean oauth_provider = false;

    @Column(nullable = false, columnDefinition = "varchar(50) default 'USER'")
    private String role = "USER";

    @Column(name = "verified")
    private boolean verified = false;
}
