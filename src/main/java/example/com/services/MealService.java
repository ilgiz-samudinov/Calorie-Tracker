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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {
    private final MealRepository mealRepository;
    private final DishRepository dishRepository;
    private final MealMapper mealMapper;
    private final Logger logger = LoggerFactory.getLogger(MealService.class);

    @Transactional
    public MealResponse addMeal(MealRequest request) {
        logger.info("Попытка добавления приема пищи для пользователя: {}", request.getUserId());

        List<Dish> dishes = dishRepository.findAllById(request.getDishIds());
        if (dishes.size() != request.getDishIds().size()) {
            logger.error("Некоторые блюда не найдены в базе данных.");
            throw new NotFoundException("Некоторые блюда не найдены в базе данных.");
        }

        Meal meal = Meal.builder()
                .userId(request.getUserId())
                .mealName(request.getMealName())
                .date(request.getDate())
                .dishes(dishes)
                .build();

        mealRepository.save(meal);
        logger.info("Прием пищи успешно добавлен с ID: {}", meal.getId());
        return mealMapper.toDto(meal);
    }

    public List<MealResponse> getUserMeals(Long userId) {
        logger.info("Получение списка приемов пищи для пользователя: {}", userId);
        return mealRepository.findByUserId(userId)
                .stream()
                .map(mealMapper::toDto)
                .toList();
    }

    @Transactional
    public MealResponse getMealById(Long userId, Long mealId) {
        logger.info("Попытка получения приема пищи с ID: {} для пользователя: {}", mealId, userId);
        Meal meal = mealRepository.findByIdAndUserId(mealId, userId)
                .orElseThrow(() -> {
                    logger.error("Прием пищи с ID {} для пользователя {} не найден", mealId, userId);
                    return new NotFoundException("Прием пищи не найден");
                });
        return mealMapper.toDto(meal);
    }

    @Transactional
    public MealResponse updateMeal(Long userId, Long mealId, MealRequest request) {
        logger.info("Попытка обновления приема пищи с ID: {} для пользователя: {}", mealId, userId);

        Meal meal = mealRepository.findByIdAndUserId(mealId, userId)
                .orElseThrow(() -> {
                    logger.error("Прием пищи с ID {} для пользователя {} не найден", mealId, userId);
                    return new NotFoundException("Прием пищи не найден");
                });

        // Проверка блюд
        List<Dish> dishes = dishRepository.findAllById(request.getDishIds());
        if (dishes.size() != request.getDishIds().size()) {
            logger.error("Некоторые блюда не найдены в базе данных.");
            throw new NotFoundException("Некоторые блюда не найдены в базе данных.");
        }

        // Обновление полей
        meal.setMealName(request.getMealName());
        meal.setDate(request.getDate());
        meal.setDishes(dishes);

        mealRepository.save(meal);
        logger.info("Прием пищи с ID {} успешно обновлен", mealId);
        return mealMapper.toDto(meal);
    }

    @Transactional
    public void deleteMeal(Long userId, Long mealId) {
        logger.info("Попытка удаления приема пищи с ID: {} для пользователя: {}", mealId, userId);

        Meal meal = mealRepository.findByIdAndUserId(mealId, userId)
                .orElseThrow(() -> {
                    logger.error("Прием пищи с ID {} для пользователя {} не найден", mealId, userId);
                    return new NotFoundException("Прием пищи не найден");
                });

        mealRepository.delete(meal);
        logger.info("Прием пищи с ID {} успешно удален", mealId);
    }
}