package example.com.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private double weight;

    @Column(nullable = false)
    private double height;

    @Enumerated(EnumType.STRING)
    private Goal goal;

    private double dailyCalorieNorm;

    @PrePersist
    @PreUpdate
    public void calculateDailyCalorieNorm(){
        this.dailyCalorieNorm = calculateCalories();
    }

    private double calculateCalories(){
        return 10 * weight + 6.25 * height - 5 * age + (goal == Goal.WEIGHT_GAIN ? 500 : goal == Goal.WEIGHT_LOSS ? -500 : 0);
    }


}
