package com.mirae.DailyBoost.challenge.controller.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChallengeRequest {
        private String title;
        private String description;
        private LocalDateTime startDate;
        private LocalDateTime endDate;

}
