package com.mirae.DailyBoost.food.domain.service;

import com.mirae.DailyBoost.food.domain.repository.Food;
import com.mirae.DailyBoost.food.domain.repository.FoodRepository;
import com.mirae.DailyBoost.food.domain.repository.enums.FoodKind;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FoodService {

  private final FoodRepository foodRepository;

  public Food register(Food food) {
    return foodRepository.save(food);
  }

  public Optional<Food> getByIdAndKind(Long id, FoodKind foodKind) {
    return foodRepository.findByIdAndFoodKind(id, foodKind);
  }

  public Optional<Food> getByIdAndName(Long id, String name) {
    return foodRepository.findByIdAndName(id, name);
  }


}
