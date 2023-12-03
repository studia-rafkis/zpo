package zpo.zpo.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import zpo.zpo.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

}
