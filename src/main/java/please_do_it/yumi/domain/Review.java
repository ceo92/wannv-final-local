package please_do_it.yumi.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@ToString(exclude = {"restaurant" , "reviewTags"})
public class Review {

  @Id @GeneratedValue
  private Long id;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  private Integer rating;

  private String content;

  private String image;

  @Column(name = "visit_date")
  private LocalDate visitDate; //방문일자

  @Column(name = "is_active")
  private Boolean isActive; //활성화 여부

  private String sentiment; //리뷰 감정분석

  private String note;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "restaurant_id")
  private Restaurant restaurant;

  @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
  private List<ReviewTag> reviewTags = new ArrayList<>();


  /**
   * 연관관계 편의 메서드
   */
  public void addRestaurant(Restaurant restaurant){
    this.restaurant = restaurant;
    restaurant.getReviews().add(this);
  }


  public void addUser(User user){
    this.user = user;
  }





}
