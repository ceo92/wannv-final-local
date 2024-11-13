package please_do_it.yumi.repository;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import please_do_it.yumi.domain.Restaurant;


@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant , Long> {

}
