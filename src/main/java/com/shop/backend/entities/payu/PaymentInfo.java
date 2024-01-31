package com.shop.backend.entities.payu;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shop.backend.entities.Order;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "payment_info")
@Data
@Getter
@Setter
public class PaymentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String payuOrderId;
    private String orderCreateDate;
    private Long totalAmount;
    private String paymentStatus;
    private String paymentId;
    private boolean isNew;
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}


