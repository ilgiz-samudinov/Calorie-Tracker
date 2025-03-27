package example.com.dto;

import example.com.models.Goal;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    private String name;

    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    @Min(value = 10, message = "Возраст не может быть меньше 10 лет")
    @Max(value = 120, message = "Возраст не может быть больше 120 лет")
    private int age;

    @Min(value = 20, message = "Вес должен быть не менее 20 кг")
    @Max(value = 300, message = "Вес не может превышать 300 кг")
    private double weight;

    @Min(value = 50, message = "Рост должен быть не менее 50 см")
    @Max(value = 250, message = "Рост не может превышать 250 см")
    private double height;

    @NotNull(message = "Цель (goal) обязательна")
    private Goal goal;
}
