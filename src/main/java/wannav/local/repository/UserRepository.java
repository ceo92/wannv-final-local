package wannav.local.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wannav.local.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
