package example.com.mappers;

import example.com.dto.UserRequest;
import example.com.dto.UserResponse;
import example.com.models.User;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .age(request.getAge())
                .weight(request.getWeight())
                .height(request.getHeight())
                .goal(request.getGoal())
                .build();
    }

    public UserResponse toDto(User user) {
        return new UserResponse(
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getWeight(),
                user.getHeight(),
                user.getGoal(),
                user.getDailyCalorieNorm()
        );
    }
}
