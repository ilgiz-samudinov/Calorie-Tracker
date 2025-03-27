package example.com.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MealRequest {
    @NotNull(message = "ID пользователя обязателен")
    private Long userId;

    @NotNull(message = "Дата и время приема пищи обязательны")
    private LocalDateTime mealTime;

    @NotNull(message = "Список блюд не может быть пустым")
    private List<Long> dishIds; // ID блюд, добавляемых в прием пищи
}
