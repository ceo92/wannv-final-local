package please_do_it.yumi;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/hello")
public class PracticeController {

    @GetMapping
    public String sdjhdfs(Model model) {
        model.addAttribute("restaurantForm", new RestaurantForm());
        return "restaurant/please";
    }

    @PostMapping
    public ResponseEntity<String> sfdudsfjkf(@ModelAttribute("restaurantForm") RestaurantForm restaurantForm) {
        List<FoodDto> foods = restaurantForm.getFoods();
        for (FoodDto food : foods) {
            System.out.println("음식 이름: " + food.getName());
            System.out.println("가격: " + food.getPrice());
            System.out.println("사진 이름: " + (food.getImage() != null ? food.getImage().getOriginalFilename() : "No file uploaded"));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @Data
    static class FoodDto{
        private String name;
        private Integer price;
        private MultipartFile image;
    }

    @Data
    static class RestaurantForm{
        private List<FoodDto> foods = new ArrayList<>();

    }
}
