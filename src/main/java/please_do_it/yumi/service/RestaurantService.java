package please_do_it.yumi.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import please_do_it.yumi.domain.Restaurant;
import please_do_it.yumi.dto.FoodSaveDto;
import please_do_it.yumi.dto.RestaurantSearchCond;
import please_do_it.yumi.repository.RestaurantRepository;

@Transactional
@Service
@RequiredArgsConstructor
public class RestaurantService {

  private final RestaurantRepository restaurantRepository;


  /*public Long save(FoodSaveDto foodSaveDto) {

  }*/


  public Restaurant findOne(Long id){
    return restaurantRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("잘못된 id 입니다."));
  }

  public List<Restaurant> findRestaurants(RestaurantSearchCond restaurantSearchCond){
    return restaurantRepository.findAll(restaurantSearchCond);
  }

}
