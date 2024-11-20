package please_do_it.yumi.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.Assertions;
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
    String nowDayOfWeek = LocalDate.now().getDayOfWeek()
        .getDisplayName(TextStyle.FULL, Locale.KOREAN); //요일 정보(월요일 , ... , 일요일 ) 담김
    System.out.println("nowDayOfWeek = " + nowDayOfWeek);
  }


  @Test
  void findAll() {
    List<Restaurant> all = restaurantRepository.findAll(
        new RestaurantSearchCond(10000, 20000, null, null, null, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), true,
            true, true, true));

    org.assertj.core.api.Assertions.assertThat(all.size()).isEqualTo(8);


  }


}