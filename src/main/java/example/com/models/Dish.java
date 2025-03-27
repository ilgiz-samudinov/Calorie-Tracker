package example.com.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dishes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer calories;

    @Column(nullable = false)
    private Double proteins;

    @Column(nullable = false)
    private Double fats;

    @Column(nullable = false)
    private Double carbohydrates;
}
