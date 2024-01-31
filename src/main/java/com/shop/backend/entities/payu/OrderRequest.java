package com.shop.backend.entities.payu;
import lombok.Data;

import java.util.List;
@Data
public class OrderRequest {
    private String notifyUrl;
    private String customerIp;
    private String merchantPosId;
    private String description;
    private String currencyCode;
    private String totalAmount;
    private Buyer buyer;
    private List<Product> products;
}

