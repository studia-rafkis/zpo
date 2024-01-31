package com.shop.backend.repositories;
import com.shop.backend.entities.Promocode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PromocodeRepository extends JpaRepository<Promocode, Long> { }
