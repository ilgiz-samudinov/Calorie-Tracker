package example.com.dto;

import example.com.models.Goal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private String name;

    private String email;

    private int age;

    private double weight;

    private double height;

    private Goal goal;

    private double dailyCalorieNorm;


}
