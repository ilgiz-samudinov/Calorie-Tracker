package example.com.repositories;

import example.com.models.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByUserId(Long userId);

    List<Meal> findByUserIdAndDate(Long userId, LocalDate date);

    List<Meal> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);


    Optional<Meal> findByIdAndUserId(Long id, Long userId);
}
