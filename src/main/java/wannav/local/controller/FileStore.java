package wannav.local.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


/**
 * multipart 파일 저장용도
 */


@Component //그냥 pc에 지정된 경로를 저장만 하네 저
@Slf4j
public class FileStore {


  //애초에 MultipartFile , @Value 이런 것들이 스프링 거를 활용하는 ㅓㄱㅅ이므러ㅗ 컴포넌트 스캔을 유도해야 하는 것!
  @Value("${restaurant.image.dir}")
  private String restaurantImageDir;

  @Value("${food.image.dir}")
  private String foodImageDir;


  /**
   * 음식 이미지 저장
   */
  public String storeFood(MultipartFile multipartFile) throws IOException {
    if (multipartFile.isEmpty()){
      log.info("multipartFile = {}", multipartFile);
      return null;
    }
    String storeFileName = getStoreFileName(multipartFile);
    String foodImagePath = getFoodImageFullPath(storeFileName);
    multipartFile.transferTo(new File(foodImagePath)); //파일 저장 로컬에선 UUID로 저장 각각을 구분짓기 위해 .png도 넣어줬음 !
    return storeFileName; //DB에 저장할 용도로 넘겨줌
  }


  /**
   * 식당 이미지들 저장
   */
  public List<String> storeRestaurants(List<MultipartFile> multipartFiles) throws IOException {
    List<String> storeFileResult = new ArrayList<>(); //계속해서 UploadFile이 생성되면 생성된 것을 계속해서 담아줘야됨
    for (MultipartFile multipartFile : multipartFiles) {
      if (!multipartFile.isEmpty()){
        String storeFileName = getStoreFileName(multipartFile);
        String restaurantImagePath = getRestaurantImageFullPath(storeFileName);
        multipartFile.transferTo(new File(restaurantImagePath));
        storeFileResult.add(restaurantImagePath); //DB에 저장할 용도로 넘겨줌
      }
    }
    return storeFileResult; //null 반환 안해도 어차피 리스트는 기본적으로 비어있는 객체로 초기화되어있으니 그냥 아닐 경우만 리턴해주면 됨!
  }

  public String getRestaurantImageFullPath(String storeFileName) {
    return restaurantImageDir + storeFileName;
  }

  public String getFoodImageFullPath(String storeFileName) {
    return foodImageDir + storeFileName;
  }


  private static String getStoreFileName(MultipartFile multipartFile) {
    String originalFilename = multipartFile.getOriginalFilename();
    String storeFileName = createStoreFileName(originalFilename);
    return storeFileName;
  }



  /**
   * 저장할 파일명만 추출
   */
  private static String createStoreFileName(String originalFilename) {
    String uuid = UUID.randomUUID().toString(); //star.jpg
    String extension = extractExtension(originalFilename); //uuid.jpg , uuid만 나오면 구분짓기 어려우므로 ! , uuid는 서버에서 동일명에 파일이름으로 인한 충돌을 방지하기 위하여 !
    return uuid + "." + extension;
  }


  /**
   * 확장자(png) 추출
   */
  private static String extractExtension(String originalFilename) {
    String[] split = originalFilename.split("\\.");


    /*int pos = originalFilename.lastIndexOf(".");
    String substring = originalFilename.substring(pos + 1);*/


    return split[split.length-1];
  }

}
