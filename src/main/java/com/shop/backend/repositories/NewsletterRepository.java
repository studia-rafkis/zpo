package com.shop.backend.repositories;
import com.shop.backend.entities.Newsletter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface NewsletterRepository extends JpaRepository<Newsletter, Long> { }
