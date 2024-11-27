package please_do_it.yumi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter(AccessLevel.PRIVATE)
public class User {
  @Id @GeneratedValue
  private Long id;

  private String username; // 유저 이름

  private String profile; //프로필 이미지

}
