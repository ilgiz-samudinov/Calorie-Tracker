package example.com.controllers;

import example.com.dto.MealRequest;
import example.com.dto.MealResponse;
import example.com.services.MealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
public class MealController {
    private final MealService mealService;

    @PostMapping
    public ResponseEntity<MealResponse> createMeal(@Valid @RequestBody MealRequest mealRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mealService.addMeal(mealRequest));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<MealResponse>> getUserMeals(@PathVariable Long userId) {
        return ResponseEntity.ok(mealService.getUserMeals(userId));
    }
}
