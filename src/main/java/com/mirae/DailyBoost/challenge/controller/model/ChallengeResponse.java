package com.mirae.DailyBoost.challenge.controller.model;

import com.mirae.DailyBoost.challenge.domain.challenge.repository.Challenge;
import com.mirae.DailyBoost.challenge.domain.challenge.repository.enums.ChallengeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeResponse {
    private Long id;
    private String title;
    private String description;
    private ChallengeStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long participantCount;

}
