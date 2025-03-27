package example.com.services;

import example.com.dto.MealRequest;
import example.com.dto.MealResponse;
import example.com.exceptions.NotFoundException;
import example.com.mappers.MealMapper;
import example.com.models.Dish;
import example.com.models.Meal;
import example.com.repositories.DishRepository;
import example.com.repositories.MealRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {
    private final MealRepository mealRepository;
    private final DishRepository dishRepository;
    private final MealMapper mealMapper;

    @Transactional
    public MealResponse addMeal(MealRequest request) {
        List<Dish> dishes = dishRepository.findAllById(request.getDishIds());
        if (dishes.isEmpty()) {
            throw new NotFoundException("Некоторые блюда не найдены в базе данных.");
        }

        Meal meal = Meal.builder()
                .userId(request.getUserId())
                .mealTime(request.getMealTime())
                .dishes(dishes)
                .build();

        mealRepository.save(meal);
        return mealMapper.toDto(meal);
    }

    public List<MealResponse> getUserMeals(Long userId) {
        return mealRepository.findByUserId(userId)
                .stream()
                .map(mealMapper::toDto)
                .toList();
    }
}
