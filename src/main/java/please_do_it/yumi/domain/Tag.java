package please_do_it.yumi.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString(exclude = {"reviewTags"})
@Getter @Setter(AccessLevel.PRIVATE)
public class Tag {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  @OneToMany(mappedBy = "tag")
  private List<ReviewTag> reviewTags = new ArrayList<>();

  private String category;

  private String name;


}
