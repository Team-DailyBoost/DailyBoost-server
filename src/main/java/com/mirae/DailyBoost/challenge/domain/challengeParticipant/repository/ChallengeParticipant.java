package com.mirae.DailyBoost.challenge.domain.challengeParticipant.repository;

import com.mirae.DailyBoost.challenge.domain.challenge.repository.Challenge;
import com.mirae.DailyBoost.user.domain.repository.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Column(name = "completed")
    private Boolean completed;

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public void markAsCompleted() {
        this.completed = true;
    }
}
