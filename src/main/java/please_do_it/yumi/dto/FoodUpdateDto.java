package please_do_it.yumi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class FoodUpdateDto {
    private String name;
    private Integer price;
    private MultipartFile foodImage; //음식 사진 폼에서 꺼내기
    private String foodImageUrl; //음식 스토리지에 저장 후 URL을 DB에 저장용


    public FoodUpdateDto(String name, int price, String image) {
        this.name = name;
        this.price=price;
        this.foodImageUrl = image;
    }
}
