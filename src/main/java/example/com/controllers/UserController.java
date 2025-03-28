package example.com.controllers;

import example.com.dto.*;
import example.com.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll(){
        return ResponseEntity.ok( userService.findAll());
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUser(id, userRequest));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/report/daily/{userId}")
    public ResponseEntity<DailyReportResponse> getDailyReport(@PathVariable Long userId,
                                                              @RequestParam
                                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DailyReportResponse dailyReport = userService.generateDailyReport(userId, date);
        return ResponseEntity.ok(dailyReport);
    }


    @GetMapping("/report/calories-check/{userId}")
    public ResponseEntity<CaloriesCheckResponse> checkCaloriesLimit(@PathVariable Long userId,
                                                                    @RequestParam
                                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        CaloriesCheckResponse checkResponse = userService.checkCaloriesLimit(userId, date);
        return ResponseEntity.ok(checkResponse);
    }


    @GetMapping("/report/history/{userId}")
    public ResponseEntity<List<DailyHistoryResponse>> getMealHistory(@PathVariable Long userId,
                                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DailyHistoryResponse> mealHistory = userService.getMealHistory(userId, startDate, endDate);
        return ResponseEntity.ok(mealHistory);
    }


}
