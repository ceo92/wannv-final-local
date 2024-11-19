package please_do_it.yumi.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import please_do_it.yumi.domain.Address;
import please_do_it.yumi.domain.BusinessDay;
import please_do_it.yumi.domain.Food;
import please_do_it.yumi.domain.Restaurant;
import please_do_it.yumi.domain.User;
import please_do_it.yumi.dto.FoodSaveDto;
import please_do_it.yumi.dto.RestaurantSaveDto;
import please_do_it.yumi.dto.RestaurantSearchCond;
import please_do_it.yumi.dto.RestaurantUpdateDto;
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

  @Transactional
  public void updateRestaurant(Long id , RestaurantUpdateDto restaurantUpdateDto) {
    //새로 만들어줬음 , 이걸로 통으로 변경
    List<BusinessDay> businessDays = BusinessDay.createBusinessDays(
        restaurantUpdateDto.getOpenTimes(),
        restaurantUpdateDto.getCloseTimes(), restaurantUpdateDto.getBreakStartTimes(),
        restaurantUpdateDto.getBreakEndTimes(), restaurantUpdateDto.getLastOrderTimes(),
        restaurantUpdateDto.getIsDayOffList());

    // 새로 만들어줬음 , 이걸로 통으로 변경
    List<Food> foods = restaurantUpdateDto.getFoodSaveDtoList().stream().map(
        foodSaveDto -> new Food(foodSaveDto.getName(), foodSaveDto.getImage(),
            foodSaveDto.getPrice())).toList();

    Restaurant updateRestaurant = findOne(id);
    updateRestaurant.changeRestaurant(restaurantUpdateDto.getBusinessNum() , restaurantUpdateDto.getRestaurantName(),
    restaurantUpdateDto.getMoodTypes(), restaurantUpdateDto.getContainFoodTypes(), restaurantUpdateDto.getProvideServiceTypes(),
    restaurantUpdateDto.getRestaurantTypes(), restaurantUpdateDto.getImage(), restaurantUpdateDto.getRoadNameAddress(),
        restaurantUpdateDto.getZipcode(),restaurantUpdateDto.getDetailsAddress(), restaurantUpdateDto.getCanPark(),
    restaurantUpdateDto.getReservationTimeGap(), restaurantUpdateDto.getIsPenalty() ,  businessDays, foods);

  }




}
