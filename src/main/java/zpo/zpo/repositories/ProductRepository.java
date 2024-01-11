package zpo.zpo.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zpo.zpo.entities.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> { }
