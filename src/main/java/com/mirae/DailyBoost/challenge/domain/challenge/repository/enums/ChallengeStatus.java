package com.mirae.DailyBoost.challenge.domain.challenge.repository.enums;

import lombok.Getter;

@Getter
public enum ChallengeStatus {
    REGISTERED("등록됨"),
    IN_PROGRESS("진행중"),
    COMPLETED("완료됨"),
    UNREGISTERED("삭제됨");

    private final String description;

    ChallengeStatus(String description) {
        this.description = description;
    }
}
