package com.shop.backend.entities.payu;
import lombok.Data;
@Data
public class CreateOrderRequest {
    private String apiKey;
    private String orderData;
}