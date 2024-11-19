package please_do_it.yumi.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import please_do_it.yumi.domain.BusinessDay;
import please_do_it.yumi.domain.Food;
import please_do_it.yumi.domain.Restaurant;
import please_do_it.yumi.domain.User;
import please_do_it.yumi.dto.FoodSaveDto;
import please_do_it.yumi.dto.RestaurantSaveDto;
import please_do_it.yumi.dto.RestaurantSearchCond;
import please_do_it.yumi.repository.RestaurantRepository;
import please_do_it.yumi.repository.UserRepository;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class RestaurantService {

  private final RestaurantRepository restaurantRepository;


  @Transactional
  public Long save(RestaurantSaveDto restaurantSaveDto) {

    List<BusinessDay> businessDays = BusinessDay.createBusinessDays(
        restaurantSaveDto.getOpenTimes(),
        restaurantSaveDto.getCloseTimes(), restaurantSaveDto.getBreakStartTimes(),
        restaurantSaveDto.getBreakEndTimes(), restaurantSaveDto.getLastOrderTimes(),
        restaurantSaveDto.getIsDayOffList());

    List<Food> foods = restaurantSaveDto.getFoodSaveDtoList()
        .stream().map(foodSaveDto -> new Food(foodSaveDto.getName() , foodSaveDto.getImage() , foodSaveDto.getPrice())).toList();


    Restaurant restaurant = Restaurant.createRestaurant(restaurantSaveDto.getBusinessNum() , restaurantSaveDto.getRestaurantName()
    , restaurantSaveDto.getMoodTypes()
    , restaurantSaveDto.getContainFoodTypes() , restaurantSaveDto.getProvideServiceTypes(),
    restaurantSaveDto.getRestaurantTypes(),
    restaurantSaveDto.getImage(),
    restaurantSaveDto.getRoadNameAddress(),
     restaurantSaveDto.getZipcode(),
    restaurantSaveDto.getDetailsAddress(),
     restaurantSaveDto.getCanPark(),
    restaurantSaveDto.getReservationTimeGap(),
    restaurantSaveDto.getIsPenalty() , businessDays , foods);

    return restaurantRepository.save(restaurant);
  }


  public Restaurant findOne(Long id){
    return restaurantRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("잘못된 id 입니다."));
  }

  public List<Restaurant> findRestaurants(RestaurantSearchCond restaurantSearchCond){
    return restaurantRepository.findAll(restaurantSearchCond);
  }

  public void updateRestaurant(){

  }




}
