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

    @GetMapping("/{userId}/{mealId}")
    public ResponseEntity<MealResponse> getMealById(
            @PathVariable Long userId,
            @PathVariable Long mealId) {
        return ResponseEntity.ok(mealService.getMealById(userId, mealId));
    }

    @PutMapping("/{userId}/{mealId}")
    public ResponseEntity<MealResponse> updateMeal(
            @PathVariable Long userId,
            @PathVariable Long mealId,
            @Valid @RequestBody MealRequest mealRequest) {
        return ResponseEntity.ok(mealService.updateMeal(userId, mealId, mealRequest));
    }

    @DeleteMapping("/{userId}/{mealId}")
    public ResponseEntity<Void> deleteMeal(
            @PathVariable Long userId,
            @PathVariable Long mealId) {
        mealService.deleteMeal(userId, mealId);
        return ResponseEntity.noContent().build();
    }
}