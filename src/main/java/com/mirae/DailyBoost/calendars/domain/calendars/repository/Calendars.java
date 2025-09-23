package com.mirae.DailyBoost.calendars.domain.calendars.repository;
import com.mirae.DailyBoost.calendars.domain.calendarMember.repository.CalendarMember;
import com.mirae.DailyBoost.user.domain.repository.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "calendars")
public class Calendars {

  @Id @Column(name = "calendar_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(length = 20)
  private String color;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_calendars_owner"))
  private User user;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "calendar",
      cascade = CascadeType.REMOVE,
      orphanRemoval = true)
  @Builder.Default
  private List<CalendarMember> members = new ArrayList<>();

  public void update(String name, String color) {
    this.name = name;
    this.color = color;
  }

  public void addMember(CalendarMember calendarMember) {
    this.members.add(calendarMember);
  }
}

