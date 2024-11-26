package please_do_it.yumi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter @Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode // 값 타입 비교 ==비교 가능하게끔
@ToString
public class Address {

  private String roadAddress;
  private String landLotAddress;
  private String detailAddress;
  private String zipCode;


}
