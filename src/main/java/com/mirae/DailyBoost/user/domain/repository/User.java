package com.mirae.DailyBoost.user.domain.repository;

import com.mirae.DailyBoost.user.domain.repository.enums.Gender;
import com.mirae.DailyBoost.user.domain.repository.enums.Role;
import com.mirae.DailyBoost.user.domain.repository.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String phone;

  @Column(nullable = false)
  private String nickname;

  @Enumerated(value = EnumType.STRING)
  private Gender gender;

  @Column(name = "last_login_at")
  private LocalDateTime lastLogin_at;

  @Column(name = "registeredAt")
  private LocalDateTime registered_at;

  @Column(name = "unregisteredAt")
  private LocalDateTime unregistered_at;

  private Long score;

  @Enumerated(value = EnumType.STRING)
  private UserStatus status;

  @Enumerated(value = EnumType.STRING)
  private Role role;

  @Embedded
  private HealthInfo healthInfo;

  private String goal;

  private LocalDate birthDay;

  private String allergy;

  private String provider; // 공급자(kakao, google.. )

  private String providerId; // 공급 아이디

  public void initInfo(HealthInfo healthInfo, String goal, String allergy) {
    this.healthInfo = healthInfo;
    this.goal = goal;
    this.allergy = allergy;

  }

  public String getRoleKey() {
    return this.role.getStr();
  }

  public void changeStatus(UserStatus status) {
    this.status = status;
  }

  public void initUnregisterAt(LocalDateTime localDateTime) {
    this.unregistered_at = localDateTime;
  }

  public void initLastLoginAt(LocalDateTime localDateTime) {
    this.lastLogin_at = localDateTime;
  }
}
