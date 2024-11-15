package please_do_it.yumi.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Tag {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  private String category;

  private String name;

}
