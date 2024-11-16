package please_do_it.yumi.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import please_do_it.yumi.domain.Restaurant;
import please_do_it.yumi.dto.RestaurantSearchCond;

@SpringBootTest
@Transactional
class RestaurantRepositoryTest {

  @Autowired RestaurantRepository restaurantRepository;

  @Rollback(value = false)
  @Test
  void save() {
  }


  @Test
  void findAll() {
    List<Integer> rates = new ArrayList<>();
    rates.add(5);
    rates.add(4);

    List<String> containFoodTypes = new ArrayList<>();
    //containFoodTypes.add()

    List<String> restaurantTypes = new ArrayList<>();
    List<String> provideServiceTypes = new ArrayList<>();
    List<String> moodTypes = new ArrayList<>();

    //restaurantRepository.findAll(new RestaurantSearchCond(1000 , 10000 , true , true , rates , "도산대로" , ));
  }


}