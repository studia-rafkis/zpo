package zpo.zpo.repositories;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zpo.zpo.entities.ProductImage;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductIdp(Long productIdp);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM products_images WHERE id = :id", nativeQuery = true)
    void deleteImage(@Param("id") Long id);


}

