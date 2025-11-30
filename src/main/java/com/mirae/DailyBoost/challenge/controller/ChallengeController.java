package com.mirae.DailyBoost.challenge.controller;

import com.mirae.DailyBoost.challenge.business.ChallengeBusiness;
import com.mirae.DailyBoost.challenge.controller.model.ChallengeRequest;
import com.mirae.DailyBoost.challenge.controller.model.ChallengeResponse;
import com.mirae.DailyBoost.challenge.controller.model.JoinDto;
import com.mirae.DailyBoost.challenge.converter.ChallengeConverter;
import com.mirae.DailyBoost.challenge.domain.challenge.repository.Challenge;
import com.mirae.DailyBoost.global.annotation.LoginUser;
import com.mirae.DailyBoost.global.api.Api;
import com.mirae.DailyBoost.global.model.MessageResponse;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.user.domain.repository.User;
import jakarta.mail.Message;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenges")
public class ChallengeController {

    private final ChallengeBusiness challengeBusiness;

    @PostMapping
    public Api<MessageResponse> createChallenge(
            @LoginUser UserDTO userDTO,
            @RequestBody ChallengeRequest challengeRequest
        ) {
        return Api.OK(challengeBusiness.createChallenge(userDTO, challengeRequest));
    }

    @PostMapping("/{challengeId}/join")
    public Api<MessageResponse> joinChallenge(
        @LoginUser UserDTO userDTO,
        @PathVariable Long challengeId
    ) {
        return Api.OK(challengeBusiness.joinChallenge(userDTO, challengeId));
    }

    @GetMapping("/all")
    public Api<List<ChallengeResponse>> getChallenges(
        @LoginUser UserDTO userDTO
    ) {
        return Api.OK(challengeBusiness.getChallenges(userDTO));
    }
}
