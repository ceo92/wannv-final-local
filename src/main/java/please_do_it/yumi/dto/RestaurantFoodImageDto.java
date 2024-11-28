package please_do_it.yumi.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RestaurantFoodImageDto {
    private List<String> restaurantImages = new ArrayList<>();
    private List<String> foodImages = new ArrayList<>();
}
