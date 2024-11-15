package please_do_it.yumi.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import please_do_it.yumi.domain.Restaurant;

@Repository
public class RestaurantRepository {

  private final EntityManager em;
  private final JPAQueryFactory query;

  @Autowired
  public RestaurantRepository(EntityManager em){
    this.em=em;
    this.query = new JPAQueryFactory(em);
  }

  public Long save(Restaurant restaurant){
    em.persist(restaurant);
    return restaurant.getId();
  }

  public Optional<Restaurant> findById(Long id){
    return Optional.ofNullable(em.find(Restaurant.class , id));
  }

  public List<Restaurant> findAll(){
    return em.createQuery("select r "
        + "from Restaurant r "
        + "join r.reviews re"
        + "join r.businessDays bd "
        + "join r.foods f " , Restaurant.class).getResultList();
  }










}
