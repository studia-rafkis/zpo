package zpo.zpo.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import zpo.zpo.entities.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}