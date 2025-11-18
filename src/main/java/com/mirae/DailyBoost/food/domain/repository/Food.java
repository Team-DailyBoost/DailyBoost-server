package com.mirae.DailyBoost.food.domain.repository;

import com.mirae.DailyBoost.food.domain.repository.enums.FoodKind;
import com.mirae.DailyBoost.food.domain.repository.enums.FoodStatus;
import com.mirae.DailyBoost.user.domain.repository.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;
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

  @Column(nullable = false)
  private Long weight;

  @Enumerated(EnumType.STRING)
  private FoodKind foodKind;

  @Enumerated(EnumType.STRING)
  private FoodStatus status;

  @Column(name = "registered_at", nullable = true)
  private LocalDate registeredAt;

  @Column(nullable = false)
  private String description;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
  private User user;

  public void changeStatus(FoodStatus foodStatus) {
    this.status = foodStatus;
  }

  public void initDate(LocalDate date) {
    this.registeredAt = date;
  }
}
