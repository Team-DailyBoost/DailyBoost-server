package com.mirae.DailyBoost.challenge.business;

import com.mirae.DailyBoost.challenge.controller.model.ChallengeRequest;
import com.mirae.DailyBoost.challenge.controller.model.ChallengeResponse;
import com.mirae.DailyBoost.challenge.controller.model.JoinDto;
import com.mirae.DailyBoost.challenge.converter.ChallengeConverter;
import com.mirae.DailyBoost.challenge.domain.challenge.repository.Challenge;
import com.mirae.DailyBoost.challenge.domain.challenge.repository.enums.ChallengeStatus;
import com.mirae.DailyBoost.challenge.service.ChallengeService;
import com.mirae.DailyBoost.global.annotation.Business;
import com.mirae.DailyBoost.global.converter.MessageConverter;
import com.mirae.DailyBoost.global.errorCode.UserErrorCode;
import com.mirae.DailyBoost.global.model.MessageResponse;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.user.domain.repository.User;
import com.mirae.DailyBoost.user.domain.service.UserService;
import com.mirae.DailyBoost.user.exception.user.UserNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Business
@Transactional
@RequiredArgsConstructor
public class ChallengeBusiness {

    private final ChallengeService challengeService;
    private final ChallengeConverter challengeConverter;
    private final UserService userService;
    private final MessageConverter messageConverter;

    public MessageResponse createChallenge(UserDTO userDTO, ChallengeRequest challengeRequest) {
        User user = userService.getById(userDTO.getId())
            .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

        Challenge challenge = challengeConverter.toEntity(user, challengeRequest);
        challenge.register();
        challengeService.createChallenge(challenge);

        return messageConverter.toResponse("도전과제가 생성되었습니다.");
    }

    public MessageResponse joinChallenge(UserDTO userDTO, Long challengeId) {
        User user = userService.getById(userDTO.getId())
            .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

        challengeService.joinChallenge(user, challengeId);
        Challenge challenge = challengeService.getChallenge(challengeId);
        return messageConverter.toResponse("도전과제에 참여되었습니다.");
    }

    public List<ChallengeResponse> getChallenges(UserDTO userDTO) {
        User user = userService.getById(userDTO.getId())
            .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

        List<Challenge> challenges = challengeService.getChallenges();

        return challengeConverter.toChallengeResponses(challenges);
    }
}
