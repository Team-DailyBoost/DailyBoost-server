package com.mirae.DailyBoost.challenge.converter;

import com.mirae.DailyBoost.challenge.controller.model.ChallengeRequest;
import com.mirae.DailyBoost.challenge.controller.model.ChallengeResponse;
import com.mirae.DailyBoost.challenge.controller.model.JoinDto;
import com.mirae.DailyBoost.challenge.domain.challenge.repository.Challenge;
import com.mirae.DailyBoost.challenge.domain.challenge.repository.enums.ChallengeStatus;
import com.mirae.DailyBoost.challenge.domain.challengeParticipant.repository.ChallengeParticipant;
import com.mirae.DailyBoost.global.annotation.Converter;
import com.mirae.DailyBoost.user.domain.repository.User;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Converter
public class ChallengeConverter {
    public Challenge toEntity(User user, ChallengeRequest request) {
        return Challenge.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .creator(user)
                .status(ChallengeStatus.REGISTERED)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public List<ChallengeResponse> toChallengeResponses(List<Challenge> challenges) {
        return challenges.stream().map(challenge ->
            ChallengeResponse.builder()
                .id(challenge.getId())
                .title(challenge.getTitle())
                .description(challenge.getDescription())
                .status(challenge.getStatus())
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .participantCount(challenge.getParticipantCount())
                .build()
        ).toList();
    }

    public ChallengeParticipant toChallengeParticipant(Challenge challenge, User user) {
        return ChallengeParticipant.builder()
                .challenge(challenge)
                .user(user)
                .joinedAt(LocalDateTime.now())
                .completed(false)
                .build();
    }

    public JoinDto toJoinResponse(Challenge challenge, User user) {
        return JoinDto.builder()
                .challengeId(challenge.getId())
                .userId(user.getId())
                .build();
    }
}
