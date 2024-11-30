package please_do_it.yumi.dto;


import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class RestaurantAdminSearchCond {

    /**
     * where 동적 조건
     */
    private String name;
    private String businessNum;
    private String restaurantTypes;
    private LocalDate createAtStart;
    private LocalDate createAtEnd;

    /**
     * having 동적 조건
     */
    private Boolean isLatest; //최신순
    private Boolean isRegister; //등록

}