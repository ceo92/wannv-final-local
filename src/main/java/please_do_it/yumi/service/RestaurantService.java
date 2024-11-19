package please_do_it.yumi.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import please_do_it.yumi.domain.BusinessDay;
import please_do_it.yumi.domain.Restaurant;
import please_do_it.yumi.dto.RestaurantSaveDto;
import please_do_it.yumi.dto.RestaurantSearchCond;
import please_do_it.yumi.repository.RestaurantRepository;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class RestaurantService {

  private final RestaurantRepository restaurantRepository;
  private final User


  @Transactional
  public Long save(RestaurantSaveDto restaurantSaveDto) {
    String restaurantName = restaurantSaveDto.getRestaurantName();
    String businessUserName = restaurantSaveDto.getBusinessUserName();
    String businessNum = restaurantSaveDto.getBusinessNum();
    List<String> moodTypes = restaurantSaveDto.getMoodTypes();
    List<String> containFoodTypes = restaurantSaveDto.getContainFoodTypes();
    List<String> provideServiceTypes = restaurantSaveDto.getProvideServiceTypes();
    List<String> restaurantTypes = restaurantSaveDto.getRestaurantTypes();
    String image = restaurantSaveDto.getImage();
    String roadNameAddress = restaurantSaveDto.getRoadNameAddress();
    String zipcode = restaurantSaveDto.getZipcode();
    String detailsAddress = restaurantSaveDto.getDetailsAddress();
    Boolean canPark = restaurantSaveDto.getCanPark();
    String reservationTimeGap = restaurantSaveDto.getReservationTimeGap();
    Boolean isPenalty = restaurantSaveDto.getIsPenalty();


    List<BusinessDay> businessDays = BusinessDay.createBusinessDays(
        restaurantSaveDto.getOpenTimes(),
        restaurantSaveDto.getCloseTimes(), restaurantSaveDto.getBreakStartTimes(),
        restaurantSaveDto.getBreakEndTimes(), restaurantSaveDto.getLastOrderTimes(),
        restaurantSaveDto.getIsDayOffList());


    restaurantRepository.save()
  }


  public Restaurant findOne(Long id){
    return restaurantRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("잘못된 id 입니다."));
  }

  public List<Restaurant> findRestaurants(RestaurantSearchCond restaurantSearchCond){
    return restaurantRepository.findAll(restaurantSearchCond);
  }




}
