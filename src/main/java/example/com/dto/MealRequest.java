package example.com.dto;

import example.com.models.MealName;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

    @Data
    @Builder
    public class MealRequest {
        @NotNull(message = "ID пользователя обязателен")
        private Long userId;
        @NotNull(message = "Названия приема пищи обязательно")
        private MealName mealName;

        @NotNull(message = "Дата и время приема пищи обязательны")
        private LocalDate date;

        @NotNull(message = "Список блюд не может быть пустым")
        private List<Long> dishIds; // ID блюд, добавляемых в прием пищи
    }
