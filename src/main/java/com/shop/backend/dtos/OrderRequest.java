package com.shop.backend.dtos;
import com.shop.backend.entities.OrderStatus;
import com.shop.backend.entities.Promocode;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    public boolean isCompany;
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
    private OrderStatus orderStatus;
    private Long promoCodeId;
    public boolean isActive;
    private List<OrderProductRequest> orderProducts;
}
