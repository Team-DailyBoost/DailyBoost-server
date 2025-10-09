package com.mirae.DailyBoost.food.domain.repository;

import com.mirae.DailyBoost.food.domain.repository.enums.FoodKind;
import com.mirae.DailyBoost.food.domain.repository.enums.FoodStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

  Optional<Food> findByIdAndFoodKind(Long id, FoodKind foodKind);

  @Query(
      """
      SELECT f
      FROM Food f
      WHERE f.user.id = :userId
      and f.name LIKE %:keyword%
      and f.status = :status
      """
  )
  List<Food> findByUser_idAndKeywordAndStatus(@RequestParam Long userId,
                                    @RequestParam String keyword,
                                    @RequestParam FoodStatus status);

  List<Food> findByUser_idAndStatusAndRegisteredAt(Long userId, FoodStatus status, LocalDate registeredAt);

  List<Food> findByUser_idAndStatusAndRegisteredAtBetweenOrderByRegisteredAtAsc(Long userId, FoodStatus status,
      LocalDate registeredAtAfter, LocalDate registeredAtBefore);
}
