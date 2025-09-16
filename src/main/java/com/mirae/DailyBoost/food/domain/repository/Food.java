package com.mirae.DailyBoost.food.domain.repository;

import com.mirae.DailyBoost.food.domain.repository.enums.FoodKind;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Food {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "food_id")
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(precision = 10, scale = 2, nullable = false)
  private BigDecimal calory;

  @Column(precision = 10, scale = 2, nullable = false)
  private BigDecimal carbohydrate;

  @Column(precision = 10, scale = 2, nullable = false)
  private BigDecimal protein;

  @Column(precision = 10, scale = 2, nullable = false)
  private BigDecimal fat;

  @Enumerated(EnumType.STRING)
  private FoodKind foodKind;

  @Column(nullable = false)
  private String description;

  // private User user;

}
