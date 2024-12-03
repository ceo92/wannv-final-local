package please_do_it.yumi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchBarDTO {
    private String regionName;
    private String restaurantName;

}
