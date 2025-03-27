package example.com.services;

import example.com.dto.DishRequest;
import example.com.dto.DishResponse;
import example.com.exceptions.DuplicateResourceException;
import example.com.mappers.DishMapper;
import example.com.models.Dish;
import example.com.repositories.DishRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final DishMapper dishMapper;

    @Transactional
    public DishResponse addDish(DishRequest request) {
        if (dishRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Блюдо с таким названием уже существует");
        }
        Dish dish = dishMapper.toEntity(request);
        dishRepository.save(dish);
        return dishMapper.toDto(dish);
    }

    @Transactional
    public List<DishResponse> getAllDishes() {
        return dishRepository.findAll()
                .stream()
                .map(dishMapper::toDto)
                .toList();
    }
}
