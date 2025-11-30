package com.mirae.DailyBoost.exercise.domain.controller;

import com.mirae.DailyBoost.exercise.domain.controller.model.request.ExerciseRequest;
import com.mirae.DailyBoost.exercise.domain.controller.model.response.ExerciseResponse;
import com.mirae.DailyBoost.food.domain.controller.model.response.FoodResponse;
import com.mirae.DailyBoost.global.annotation.LoginUser;
import com.mirae.DailyBoost.global.api.Api;
import com.mirae.DailyBoost.exercise.domain.business.ExerciseBusiness;
import com.mirae.DailyBoost.exercise.domain.controller.model.response.ExerciseRecommendation;
import com.mirae.DailyBoost.global.model.MessageResponse;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.exercise.domain.controller.model.request.PartExerciseRequest;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exercise")
public class ExerciseController {
    private final ExerciseBusiness exerciseBusiness;

    @PostMapping("/register/{exerciseId}")
    public Api<MessageResponse> register(@LoginUser UserDTO userDTO, @PathVariable Long exerciseId) {
        return Api.OK(exerciseBusiness.register(userDTO, exerciseId));
    }

    @PostMapping("/unregister/{exerciseId}")
    public Api<MessageResponse> unregister(@LoginUser UserDTO userDTO, @PathVariable Long exerciseId) {
        return Api.OK(exerciseBusiness.unregister(userDTO, exerciseId));
    }

    @PostMapping("/complete/{exerciseId}")
    public Api<MessageResponse> complete(@LoginUser UserDTO userDTO, @PathVariable Long exerciseId) {
        return Api.OK(exerciseBusiness.complete(userDTO, exerciseId));
    }

    @PostMapping("/uncomplete/{exerciseId}")
    public Api<MessageResponse> uncomplete(@LoginUser UserDTO userDTO, @PathVariable Long exerciseId) {
        return Api.OK(exerciseBusiness.uncomplete(userDTO, exerciseId));
    }

    @PostMapping("/part/recommend")
    public Api<List<ExerciseRecommendation>> recommendPartExercise(@LoginUser UserDTO userDTO, @RequestBody PartExerciseRequest exerciseRequest) {
        return Api.OK(exerciseBusiness.recommendPartExercise(userDTO, exerciseRequest));

    }

    @PostMapping("/recommend")
    public Api<List<ExerciseRecommendation>> recommendExercise(@LoginUser UserDTO userDTO, @RequestBody ExerciseRequest exerciseRequest) {
        return Api.OK(exerciseBusiness.recommendExercise(userDTO, exerciseRequest));
    }

    @GetMapping("/{exerciseId:[0-9]+}") //api/exercise/{exerciseId}
    public Api<ExerciseResponse> getExercise(@LoginUser UserDTO userDTO, @PathVariable Long exerciseId) {
        return Api.OK(exerciseBusiness.getExercise(userDTO, exerciseId));
    }

    @GetMapping("/today")
    public Api<List<ExerciseResponse>> getByToday(@LoginUser UserDTO userDTO) {
        return Api.OK(exerciseBusiness.getByToday(userDTO));
    }


}
