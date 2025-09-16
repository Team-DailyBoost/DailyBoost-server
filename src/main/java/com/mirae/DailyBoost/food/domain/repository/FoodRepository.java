package com.mirae.DailyBoost.food.domain.repository;

import com.mirae.DailyBoost.food.domain.repository.enums.FoodKind;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

  Optional<Food> findByIdAndFoodKind(Long id, FoodKind foodKind);

  Optional<Food> findByIdAndName(Long id, String name);

}
