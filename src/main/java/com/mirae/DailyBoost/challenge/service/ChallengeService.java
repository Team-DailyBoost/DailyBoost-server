package com.mirae.DailyBoost.challenge.service;

import com.mirae.DailyBoost.challenge.domain.challenge.repository.Challenge;
import com.mirae.DailyBoost.challenge.domain.challenge.repository.ChallengeRepository;
import com.mirae.DailyBoost.challenge.domain.challenge.repository.enums.ChallengeStatus;
import com.mirae.DailyBoost.challenge.domain.challengeParticipant.repository.ChallengeParticipant;
import com.mirae.DailyBoost.challenge.domain.challengeParticipant.repository.ChallengeParticipantRepository;
import com.mirae.DailyBoost.global.errorCode.UserErrorCode;
import com.mirae.DailyBoost.user.domain.repository.User;
import com.mirae.DailyBoost.user.domain.service.UserService;
import com.mirae.DailyBoost.user.exception.user.UserNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeParticipantRepository challengeParticipantRepository;
    private final UserService userService;

    public Challenge createChallenge(Challenge challenge) {
        return challengeRepository.save(challenge);
    }

    @Transactional(readOnly = true)
    public Challenge getChallenge(Long challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found"));
    }

    public void joinChallenge(User user, Long challengeId) {
        Challenge challenge = getChallenge(challengeId);
        User userToJoin = userService.getById(user.getId()).orElseThrow(() -> new UserNotFoundException(
            UserErrorCode.USER_NOT_FOUND));

        Optional<ChallengeParticipant> existingParticipant = challengeParticipantRepository.findByChallengeAndUser(challenge, userToJoin);
        if (existingParticipant.isPresent()) {
            throw new IllegalStateException("User has already joined this challenge");
        }

        ChallengeParticipant participant = ChallengeParticipant.builder()
                .challenge(challenge)
                .user(userToJoin)
                .build();

        challenge.addParticipant(participant);
        challengeRepository.save(challenge);
    }

    public List<Challenge> getChallenges() {
        return challengeRepository.findAllByStatus(ChallengeStatus.REGISTERED);
    }
}
