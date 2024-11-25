package please_do_it.yumi.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FoodSaveDto {

  private String name;
  private Integer price;
  private MultipartFile image; //음식 사진은 한장

}
