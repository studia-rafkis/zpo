package com.shop.backend.repositories;

import com.shop.backend.dtos.OrderPaymentInfoDto;
import com.shop.backend.entities.Order;
import com.shop.backend.entities.Product;
import com.shop.backend.entities.payu.PaymentInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByEmail(String email);
    @Query("SELECT new com.shop.backend.dtos.OrderPaymentInfoDto(o, p) FROM Order o JOIN o.paymentInfo p")
    List<OrderPaymentInfoDto> findAllWithPaymentInfo();
}
