package com.shop.backend.repositories;
import com.shop.backend.entities.Order;
import com.shop.backend.entities.payu.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {
    Optional<PaymentInfo> findByOrder_Ido(Long ido);
}