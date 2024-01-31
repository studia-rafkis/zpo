package com.shop.backend.repositories;
import com.shop.backend.entities.Comments;
import com.shop.backend.entities.CommentsLikes;
import com.shop.backend.entities.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentLikesRepository extends JpaRepository<CommentsLikes, Long> {
}