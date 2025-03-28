package example.com.services;

import example.com.dto.DishRequest;
import example.com.dto.DishResponse;
import example.com.exceptions.DuplicateResourceException;
import example.com.exceptions.NotFoundException;
import example.com.mappers.DishMapper;
import example.com.models.Dish;
import example.com.repositories.DishRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final DishMapper dishMapper;
    private final Logger logger = LoggerFactory.getLogger(DishService.class);

    @Transactional
    public DishResponse addDish(DishRequest request) {
        logger.info("Попытка добавления блюда с названием: {}", request.getName());
        if (dishRepository.existsByName(request.getName())) {
            logger.error("Блюдо с таким названием уже существует: {}", request.getName());
            throw new DuplicateResourceException("Блюдо с таким названием уже существует");
        }
        Dish dish = dishMapper.toEntity(request);
        dishRepository.save(dish);
        logger.info("Блюдо успешно добавлено с названием: {}", dish.getName());
        return dishMapper.toDto(dish);
    }

    @Transactional
    public List<DishResponse> getAllDishes() {
        logger.info("Запрос на получение всех блюд.");
        return dishRepository.findAll()
                .stream()
                .map(dishMapper::toDto)
                .toList();
    }

    @Transactional
    public DishResponse getDishById(Long id) {
        logger.info("Попытка получить блюдо с id: {}", id);
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Блюдо с id {} не найдено.", id);
                    return new NotFoundException("Блюдо с id " + id + " не найдено");
                });
        return dishMapper.toDto(dish);
    }

    @Transactional
    public DishResponse updateDish(Long id, DishRequest request) {
        logger.info("Попытка обновления блюда с id: {}", id);
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Блюдо с id {} не найдено.", id);
                    return new NotFoundException("Блюдо с id " + id + " не найдено");
                });

        // Проверка на уникальность названия перед обновлением
        if (!dish.getName().equals(request.getName()) && dishRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Блюдо с таким названием уже существует");
        }

        dish.setName(request.getName());
        // Здесь можно добавить другие обновления полей, если необходимо

        dishRepository.save(dish);
        logger.info("Блюдо с id={} успешно обновлено", id);
        return dishMapper.toDto(dish);
    }

    @Transactional
    public void deleteDish(Long id) {
        logger.info("Попытка удаления блюда с id: {}", id);
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Блюдо с id {} не найдено для удаления.", id);
                    return new NotFoundException("Блюдо с id " + id + " не найдено");
                });
        dishRepository.delete(dish);
        logger.info("Блюдо с id={} успешно удалено", id);
    }
}