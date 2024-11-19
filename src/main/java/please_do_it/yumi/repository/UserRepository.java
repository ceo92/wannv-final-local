package please_do_it.yumi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import please_do_it.yumi.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
