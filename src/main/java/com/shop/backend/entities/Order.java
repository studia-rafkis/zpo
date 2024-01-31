package com.shop.backend.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shop.backend.entities.payu.PaymentInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ido;
    private boolean isCompany;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String street;
    private String streetNumber;
    private String houseNumber;
    private String country;
    private String state;
    private String zipCode;
    private String firstName2;
    private String lastName2;
    private String email2;
    private String phone2;
    private String street2;
    private String streetNumber2;
    private String houseNumber2;
    private String country2;
    private String state2;
    private String zipCode2;
    private String orderNotes;
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @JsonIgnore

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private PaymentInfo paymentInfo;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "promocode_id")
    private Promocode promocode;
}


