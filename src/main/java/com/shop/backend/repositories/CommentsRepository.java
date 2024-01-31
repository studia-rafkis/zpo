package com.shop.backend.repositories;
import com.shop.backend.entities.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> { }
