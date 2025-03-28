package example.com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@AllArgsConstructor
@Data
public class DailyHistoryResponse {
    private LocalDate date;
    private int totalCalories;
}
