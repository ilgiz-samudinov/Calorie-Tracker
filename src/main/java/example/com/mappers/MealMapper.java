
package example.com.mappers;

import example.com.dto.MealResponse;
import example.com.models.Meal;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MealMapper {
    private final DishMapper dishMapper = new DishMapper();

    public MealResponse toDto(Meal meal) {
        return new MealResponse(
                meal.getId(),
                meal.getUserId(),
                meal.getMealName(),
                meal.getDate(),
                meal.getDishes().stream().map(dishMapper::toDto).collect(Collectors.toList())
        );
    }
}
