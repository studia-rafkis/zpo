package com.shop.backend.repositories;
import com.shop.backend.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}