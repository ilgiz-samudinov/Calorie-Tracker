package example.com.services;

import example.com.dto.*;
import example.com.exceptions.DuplicateResourceException;
import example.com.exceptions.NotFoundException;
import example.com.mappers.UserMapper;
import example.com.models.Dish;
import example.com.models.Meal;
import example.com.models.User;
import example.com.repositories.MealRepository;
import example.com.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final MealRepository mealRepository;

    @Transactional
    public UserResponse registerUser(UserRequest userRequest) {
        logger.info("Попытка регистрации пользователя с email: {}", userRequest.getEmail());
        validateUniqueness(userRequest.getName(), userRequest.getEmail());
        User savedUser = mapper.toEntity(userRequest);
        userRepository.save(savedUser);
        logger.info("Пользователь успешно зарегистрирован с email: {}", savedUser.getEmail());
        return mapper.toDto(savedUser);
    }

    @Transactional
    public List<UserResponse> findAll() {
        logger.info("Запрос на получение всех пользователей.");
        return userRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        logger.info("Попытка обновления пользователя с id: {}", id);

        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Пользователь с id {} не найден.", id);
                    return new NotFoundException("Пользователь с id " + id + " не найден");
                });

        validateUniquenessForUpdate(id, userRequest.getName(), userRequest.getEmail());
        updateEntityFromRequest(foundUser, userRequest);

        userRepository.save(foundUser);
        logger.info("Пользователь с id={} успешно обновлен", id);
        return mapper.toDto(foundUser);
    }

    @Transactional
    public void delete(Long id) {
        logger.info("Попытка удаления пользователя с id: {}", id);

        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Пользователь с id {} не найден для удаления.", id);
                    return new NotFoundException("Пользователь с id " + id + " не найден");
                });

        userRepository.delete(foundUser);
        logger.info("Пользователь с id={} успешно удален", id);
    }

    private void validateUniqueness(String name, String email) {
        logger.info("Проверка уникальности для имени: {} и email: {}", name, email);
        if (userRepository.existsByName(name)) {
            logger.warn("Попытка регистрации с уже существующим именем: {}", name);
            throw new DuplicateResourceException("Пользователь с таким именем уже существует");
        }
        if (userRepository.existsByEmail(email)) {
            logger.warn("Попытка регистрации с уже существующим email: {}", email);
            throw new DuplicateResourceException("Пользователь с таким email уже существует!");
        }
        logger.info("Имя и email уникальны.");
    }

    private void validateUniquenessForUpdate(Long id, String name, String email) {
        logger.info("Проверка уникальности для обновления пользователя с id: {}", id);
        if (userRepository.existsByNameAndIdNot(name, id)) {
            logger.warn("Попытка обновления с уже существующим именем: {}", name);
            throw new DuplicateResourceException("Пользователь с таким именем уже существует: " + name);
        }
        if (userRepository.existsByEmailAndIdNot(email, id)) {
            logger.warn("Попытка обновления с уже существующим email: {}", email);
            throw new DuplicateResourceException("Пользователь с таким email уже существует: " + email);
        }
        logger.info("Имя и email уникальны для обновления.");
    }

    private void updateEntityFromRequest(User user, UserRequest request) {
        logger.info("Обновление данных пользователя с id: {}", user.getId());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        user.setHeight(request.getHeight());
        user.setWeight(request.getWeight());
        user.setGoal(request.getGoal());
        logger.info("Данные пользователя с id: {} успешно обновлены", user.getId());
    }

    public DailyReportResponse generateDailyReport(Long userId, LocalDate date) {
        logger.info("Генерация ежедневного отчета для пользователя с id: {} на дату: {}", userId, date);
        List<Meal> meals = mealRepository.findByUserIdAndDate(userId, date);

        int totalCalories = meals.stream()
                .flatMap(meal -> Optional.ofNullable(meal.getDishes()).orElse(Collections.emptyList()).stream())
                .mapToInt(Dish::getCalories)
                .sum();

        List<DailyReportResponse.MealReport> mealReports = meals.stream()
                .map(meal -> new DailyReportResponse.MealReport(
                        meal.getDate(),
                        meal.getMealName(),
                        meal.getDishes().stream()
                                .mapToInt(Dish::getCalories)
                                .sum()
                ))
                .toList();

        logger.info("Ежедневный отчет для пользователя с id: {} на дату: {} с суммарным количеством калорий: {}", userId, date, totalCalories);
        return new DailyReportResponse(date, totalCalories, mealReports);
    }

    public CaloriesCheckResponse checkCaloriesLimit(Long userId, LocalDate date) {
        logger.info("Проверка лимита калорий для пользователя с id: {} на дату: {}", userId, date);
        DailyReportResponse dailyReport = generateDailyReport(userId, date);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        double userCaloriesLimit = user.getDailyCalorieNorm();
        boolean isWithinLimit = dailyReport.getTotalCalories() <= userCaloriesLimit;

        logger.info("Пользователь с id: {} имеет лимит калорий: {}. Превышен ли лимит: {}", userId, userCaloriesLimit, isWithinLimit);
        return new CaloriesCheckResponse(date, (int) userCaloriesLimit, dailyReport.getTotalCalories(), isWithinLimit);
    }

    public List<DailyHistoryResponse> getMealHistory(Long userId, LocalDate startDate, LocalDate endDate) {
        logger.info("Запрос на историю приемов пищи для пользователя с id: {} с {} по {}", userId, startDate, endDate);
        List<Meal> meals = mealRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        Map<LocalDate, Integer> dailyCaloriesMap = new HashMap<>();

        for (Meal meal : meals) {
            LocalDate mealDate = meal.getDate();
            int mealCalories = meal.getDishes().stream()
                    .mapToInt(Dish::getCalories)
                    .sum();

            dailyCaloriesMap.put(mealDate, dailyCaloriesMap.getOrDefault(mealDate, 0) + mealCalories);
        }

        List<DailyHistoryResponse> history = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : dailyCaloriesMap.entrySet()) {
            history.add(new DailyHistoryResponse(entry.getKey(), entry.getValue()));
        }
        logger.info("История приемов пищи для пользователя с id: {} на период с {} по {} получена.", userId, startDate, endDate);
        return history;
    }
}
