package example.com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class MealResponse {
    private Long id;
    private Long userId;
    private LocalDateTime mealTime;
    private List<DishResponse> dishes;
}
