package example.com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class CaloriesCheckResponse {
    private LocalDate date;
    private int userCaloriesLimit;
    private int totalCalories;
    private boolean isWithinLimit;
}

