package example.com.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DishRequest {
    @NotBlank(message = "Название блюда не может быть пустым")
    private String name;

    @NotNull(message = "Количество калорий обязательно")
    @Min(value = 0, message = "Калории не могут быть отрицательными")
    private Integer calories;

    @NotNull(message = "Белки обязательны")
    @Min(value = 0, message = "Белки не могут быть отрицательными")
    private Double proteins;

    @NotNull(message = "Жиры обязательны")
    @Min(value = 0, message = "Жиры не могут быть отрицательными")
    private Double fats;

    @NotNull(message = "Углеводы обязательны")
    @Min(value = 0, message = "Углеводы не могут быть отрицательными")
    private Double carbohydrates;
}
