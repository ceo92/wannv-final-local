package please_do_it.yumi.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
@AllArgsConstructor
public class Address {

  private String roadAddress;
  private String landLotAddress;
  private String detailAddress;
  private String zipCode;


}
