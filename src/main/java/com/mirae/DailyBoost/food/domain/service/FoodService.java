package com.mirae.DailyBoost.food.domain.service;

import static java.time.temporal.TemporalAdjusters.previousOrSame;

import com.mirae.DailyBoost.food.domain.repository.Food;
import com.mirae.DailyBoost.food.domain.repository.FoodRepository;
import com.mirae.DailyBoost.food.domain.repository.enums.FoodKind;
import com.mirae.DailyBoost.food.domain.repository.enums.FoodStatus;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FoodService {

  private final FoodRepository foodRepository;


  public Food save(Food food) {
    return foodRepository.save(food);
  }

  public List<Food> saveAll(List<Food> foods) {
    return foodRepository.saveAll(foods);
  }

  public Optional<Food> getByIdAndKind(Long id, FoodKind foodKind) {
    return foodRepository.findByIdAndFoodKind(id, foodKind);
  }

  public List<Food> getByKeywordAndStatus(Long userId, String keyword, FoodStatus status) {
    return foodRepository.findByUser_idAndKeywordAndStatus(userId, keyword, status);
  }


  public Optional<Food> getById(Long id) {
    return foodRepository.findById(id);
  }

  public List<Food> getByStatusAndToday(Long userId, FoodStatus status, LocalDate date) {
    return foodRepository.findByUser_idAndStatusAndRegisteredAt(userId, status, date);

  }

  public List<Food> getByStatusAndWeekly(Long userId, FoodStatus foodStatus) {
    LocalDate today = LocalDate.now();
    LocalDate start = today.with(previousOrSame(DayOfWeek.MONDAY));
    LocalDate end   = start.plusDays(6); // 일요일

    return foodRepository.findByUser_idAndStatusAndRegisteredAtBetweenOrderByRegisteredAtAsc(userId, foodStatus, today, end);
  }
}
