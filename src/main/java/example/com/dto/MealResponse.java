package example.com.dto;

import example.com.models.MealName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class MealResponse {
    private Long id;
    private Long userId;
    private MealName mealName;
    private LocalDate date;
    private List<DishResponse> dishes;
}
