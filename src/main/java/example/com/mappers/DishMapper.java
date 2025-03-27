package example.com.mappers;

import example.com.dto.DishRequest;
import example.com.dto.DishResponse;
import example.com.models.Dish;
import org.springframework.stereotype.Component;

@Component
public class DishMapper {
    public Dish toEntity(DishRequest request) {
        return Dish.builder()
                .name(request.getName())
                .calories(request.getCalories())
                .proteins(request.getProteins())
                .fats(request.getFats())
                .carbohydrates(request.getCarbohydrates())
                .build();
    }

    public DishResponse toDto(Dish dish) {
        return new DishResponse(
                dish.getId(),
                dish.getName(),
                dish.getCalories(),
                dish.getProteins(),
                dish.getFats(),
                dish.getCarbohydrates()
        );
    }
}
