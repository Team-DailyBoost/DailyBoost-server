package com.mirae.DailyBoost.user.domain.repository;

import com.mirae.DailyBoost.user.domain.repository.enums.Goal;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthInfo {

  private BigDecimal weight;
  private BigDecimal height;

  @Enumerated(value = EnumType.STRING)
  private Goal goal;

}
