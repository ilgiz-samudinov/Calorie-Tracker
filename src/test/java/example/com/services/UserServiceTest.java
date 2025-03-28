package example.com.services;

import example.com.dto.*;
import example.com.exceptions.DuplicateResourceException;
import example.com.exceptions.NotFoundException;
import example.com.mappers.UserMapper;
import example.com.models.Goal;
import example.com.models.User;
import example.com.repositories.MealRepository;
import example.com.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserRequest userRequest;
    private User user;

    @BeforeEach
    void setUp() {
        // Использование builder для создания объектов
        userRequest = UserRequest.builder()
                .name("Test User")
                .email("test@example.com")
                .age(25)
                .height(180)
                .weight(75)
                .goal(Goal.WEIGHT_LOSS)
                .build();

        user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .age(25)
                .height(180)
                .weight(75)
                .goal(Goal.WEIGHT_LOSS)
                .build();
    }

    @Test
    void testRegisterUser() {
        // Мокируем поведение репозитория
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByName(userRequest.getName())).thenReturn(false);
        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(UserResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .height(user.getHeight())
                .weight(user.getWeight())
                .goal(user.getGoal())
                .build());

        // Выполняем тестируемый метод
        UserResponse result = userService.registerUser(userRequest);

        // Проверяем результаты
        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());

        verify(userRepository).save(user);  // Проверка, что метод save был вызван
    }

    @Test
    void testRegisterUserWithExistingEmail() {
        // Мокируем существующий email
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(true);

        // Проверяем, что выбрасывается исключение DuplicateResourceException
        assertThrows(DuplicateResourceException.class, () -> userService.registerUser(userRequest));
    }

    @Test
    void testFindAllUsers() {
        // Мокируем поведение репозитория
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDto(user)).thenReturn(UserResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .height(user.getHeight())
                .weight(user.getWeight())
                .goal(user.getGoal())
                .build());

        // Выполняем тестируемый метод
        List<UserResponse> result = userService.findAll();

        // Проверяем результаты
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getName(), result.get(0).getName());
    }


    @Test
    void testUpdateUser() {
        // 1. Подготовка данных
        Long userId = 1L;
        User originalUser = User.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .age(25)
                .height(180)
                .weight(75)
                .goal(Goal.WEIGHT_LOSS)
                .build();

        UserRequest updatedRequest = UserRequest.builder()
                .name("Updated User")
                .email("updated@example.com")
                .age(26)
                .height(185)
                .weight(80)
                .goal(Goal.MAINTENANCE)
                .build();

        User updatedUser = User.builder()
                .id(userId)
                .name("Updated User")
                .email("updated@example.com")
                .age(26)
                .height(185)
                .weight(80)
                .goal(Goal.MAINTENANCE)
                .build();

        // 2. Настройка моков
        when(userRepository.findById(userId)).thenReturn(Optional.of(originalUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser); // Важно: возвращаем ОБНОВЛЕННЫЙ объект
        when(userMapper.toDto(updatedUser)).thenReturn(
                UserResponse.builder()
                        .name("Updated User")
                        .email("updated@example.com")
                        .age(26)
                        .height(185)
                        .weight(80)
                        .goal(Goal.MAINTENANCE)
                        .build()
        );

        // 3. Вызов метода
        UserResponse result = userService.updateUser(userId, updatedRequest);

        // 4. Проверки
        assertEquals("Updated User", result.getName());

        // Дополнительная проверка: убеждаемся, что save вызван с обновленными данными
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("Updated User", savedUser.getName());
        assertEquals("updated@example.com", savedUser.getEmail());
    }



    @Test
    void testUpdateUserNotFound() {
        // Мокируем, что пользователь не найден
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Проверяем, что выбрасывается исключение NotFoundException
        assertThrows(NotFoundException.class, () -> userService.updateUser(user.getId(), userRequest));
    }

    @Test
    void testDeleteUser() {
        // Мокируем поведение репозитория
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Выполняем тестируемый метод
        userService.delete(user.getId());

        // Проверяем, что метод delete был вызван
        verify(userRepository).delete(user);
    }

    @Test
    void testDeleteUserNotFound() {
        // Мокируем, что пользователь не найден
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Проверяем, что выбрасывается исключение NotFoundException
        assertThrows(NotFoundException.class, () -> userService.delete(user.getId()));
    }
}
