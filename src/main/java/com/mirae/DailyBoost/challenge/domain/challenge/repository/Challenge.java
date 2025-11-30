package com.mirae.DailyBoost.challenge.domain.challenge.repository;

import com.mirae.DailyBoost.challenge.domain.challenge.repository.enums.ChallengeStatus;
import com.mirae.DailyBoost.challenge.domain.challengeParticipant.repository.ChallengeParticipant;
import com.mirae.DailyBoost.user.domain.repository.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User creator;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChallengeStatus status;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "unregistered_at")
    private LocalDateTime unregisteredAt;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ChallengeParticipant> participants = new ArrayList<>();

    @Builder.Default
    @Column(nullable = false)
    private Long participantCount = 0L;

    public void updateInfo(String title, String description, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.updatedAt = LocalDateTime.now();
    }

    public void register() {
        this.participantCount++;
    }

    public void unregister() {
        this.status = ChallengeStatus.UNREGISTERED;
        this.unregisteredAt = LocalDateTime.now();
    }

    public void addParticipant(ChallengeParticipant participant) {
        this.participants.add(participant);
        this.participantCount++;
    }

    public void removeParticipant(ChallengeParticipant participant) {
        this.participants.remove(participant);
        this.participantCount--;
    }

    public void startChallenge() {
        this.status = ChallengeStatus.IN_PROGRESS;
    }

    public void completeChallenge() {
        this.status = ChallengeStatus.COMPLETED;
    }
}
