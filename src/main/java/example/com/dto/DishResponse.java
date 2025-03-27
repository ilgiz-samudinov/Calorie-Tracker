package example.com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DishResponse {
    private Long id;
    private String name;
    private Integer calories;
    private Double proteins;
    private Double fats;
    private Double carbohydrates;
}
