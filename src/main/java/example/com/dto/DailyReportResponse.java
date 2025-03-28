package example.com.dto;

import example.com.models.MealName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
public class DailyReportResponse {
    private LocalDate date;
    private int totalCalories;
    private List<MealReport> meals;

    @Data
    @AllArgsConstructor
    public static class MealReport {
        private LocalDate date;
        private MealName mealName;
        private int calories;
    }
}
