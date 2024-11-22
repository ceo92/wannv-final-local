package please_do_it.yumi.service;

import static please_do_it.yumi.domain.QBusinessDay.businessDay;

import com.querydsl.core.Tuple;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import please_do_it.yumi.constant.BusinessStatus;
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
     restaurantSaveDto.getLandLotAddress(),
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

  public List<Tuple> findRestaurants(RestaurantSearchCond restaurantSearchCond){
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
    restaurantUpdateDto.getRestaurantTypes(), restaurantUpdateDto.getImage(), restaurantUpdateDto.getRoadNameAddress(), restaurantUpdateDto.getLandLotAddress(),
        restaurantUpdateDto.getZipcode(),restaurantUpdateDto.getDetailsAddress(), restaurantUpdateDto.getCanPark(),
    restaurantUpdateDto.getReservationTimeGap(), restaurantUpdateDto.getIsPenalty() ,  businessDays, foods);

  }

  // 프로세스 : 현재 요일과는 아무 상관없음 , 그냥 전체에 대한 로직임
  @Transactional
  @Scheduled(cron = "0 */30 * * * *")
  public void updateBusinessStatus(){
    LocalDateTime now = LocalDateTime.now();
    List<Restaurant> restaurants = restaurantRepository.findAll();
    //다 계산해주는 거니까 여기서 그냥 모든 상태를 이 메서드 안에서 동시에 업데이트 해주는 것!

    //아니네 휴무일 계산은 마지막에 해줘야하네
    for (Restaurant restaurant : restaurants) {
      List<BusinessDay> businessDays = restaurant.getBusinessDays(); //한 식당에 대한 영업일들(7일)
      for (BusinessDay businessDay : businessDays) {

        LocalTime openTime = businessDay.getOpenTime();
        LocalTime closeTime = businessDay.getCloseTime();
        LocalTime breakStartTime = businessDay.getBreakStartTime();
        LocalTime breakEndTime = businessDay.getBreakEndTime();
        Boolean isDayOff = businessDay.getIsDayOff();

        if (isDayOff.equals(true)){
          restaurant.changeBusinessStatus(BusinessStatus.DAY_OFF);
          return; //휴무일이면 아래로 내려가지 않고 바로 종료되게 , 30분마다 스케줄링해서 휴무일 시 계속해서 리턴됨
        }

        if ((now.toLocalTime().isAfter(openTime) && now.toLocalTime().isBefore(breakStartTime))
            || (now.toLocalTime().isAfter(breakEndTime) && now.toLocalTime().isBefore(closeTime))) {
          restaurant.changeBusinessStatus(BusinessStatus.OPEN);
        }

        else if (now.toLocalTime().isAfter(breakStartTime) && now.toLocalTime().isBefore(breakEndTime)) {
          restaurant.changeBusinessStatus(BusinessStatus.BREAK_TIME);
        }
        else {
          restaurant.changeBusinessStatus(BusinessStatus.CLOSE);
        }
      }
    }
  }




}
