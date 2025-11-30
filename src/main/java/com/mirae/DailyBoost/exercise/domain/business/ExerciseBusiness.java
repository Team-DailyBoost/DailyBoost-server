package com.mirae.DailyBoost.exercise.domain.business;

import com.mirae.DailyBoost.exercise.domain.controller.model.request.ExerciseRequest;
import com.mirae.DailyBoost.exercise.domain.controller.model.response.ExerciseResponse;
import com.mirae.DailyBoost.exercise.domain.repository.enums.ExerciseStatus;
import com.mirae.DailyBoost.global.annotation.Business;
import com.mirae.DailyBoost.global.converter.MessageConverter;
import com.mirae.DailyBoost.exercise.domain.controller.model.response.ExerciseRecommendation;
import com.mirae.DailyBoost.exercise.domain.converter.ExerciseConverter;
import com.mirae.DailyBoost.exercise.domain.repository.Exercise;
import com.mirae.DailyBoost.exercise.domain.service.ExerciseService;
import com.mirae.DailyBoost.global.errorCode.UserErrorCode;
import com.mirae.DailyBoost.global.model.MessageResponse;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.exercise.domain.controller.model.request.PartExerciseRequest;
import com.mirae.DailyBoost.user.domain.repository.User;
import com.mirae.DailyBoost.user.domain.service.UserService;
import com.mirae.DailyBoost.user.exception.user.UserNotFoundException;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.transaction.annotation.Transactional;

@Business
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class ExerciseBusiness {

  private final ExerciseService exerciseService;
  private final UserService userService;
  private final ExerciseConverter exerciseConverter;
  private final MessageConverter messageConverter;

  @Qualifier("geminiClient")
  private final ChatClient chatClient;

  public MessageResponse register(UserDTO userDTO, Long exerciseId) {
    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

    Exercise exercise = exerciseService.getExercise(exerciseId, ExerciseStatus.UNREGISTERED)
        .orElseThrow(() -> new IllegalArgumentException("EXERCISE_NOT_FOUND"));

    if (exercise.getUser().getId() != user.getId()) {
      throw new IllegalArgumentException("등록할 권한이 없습니다.");
    }

    exerciseService.register(exercise);

    return messageConverter.toResponse("운동 기록이 등록되었습니다.");


  }

  public MessageResponse unregister(UserDTO userDTO, Long exerciseId) {
    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

    Exercise exercise = exerciseService.getExercise(exerciseId, ExerciseStatus.UNREGISTERED)
        .orElseThrow(() -> new IllegalArgumentException("EXERCISE_NOT_FOUND"));

    if (exercise.getUser().getId() != user.getId()) {
      throw new IllegalArgumentException("삭제 권한이 없습니다.");
    }

    exercise.unregister();
    exerciseService.save(exercise);

    return messageConverter.toResponse("운동 기록이 삭제되었습니다.");

  }

  public MessageResponse complete(UserDTO userDTO, Long exerciseId) {
    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

    Exercise exercise = exerciseService.getExercise(exerciseId, ExerciseStatus.REGISTERED)
        .orElseThrow(() -> new IllegalArgumentException("EXERCISE_NOT_FOUND"));

    if (exercise.getUser().getId() != user.getId()) {
      throw new IllegalArgumentException("편집할 권한이 없습니다.");
    }

    exercise.completed();
    exerciseService.save(exercise);

    return messageConverter.toResponse("운동 완료");

  }

  public MessageResponse uncomplete(UserDTO userDTO, Long exerciseId) {

    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

    Exercise exercise = exerciseService.getExercise(exerciseId, ExerciseStatus.REGISTERED)
        .orElseThrow(() -> new IllegalArgumentException("EXERCISE_NOT_FOUND"));

    if (exercise.getUser().getId() != user.getId()) {
      throw new IllegalArgumentException("편집할 권한이 없습니다.");
    }

    exercise.uncompleted();
    exerciseService.save(exercise);

    return messageConverter.toResponse("운동 미완료");
  }

  public List<ExerciseResponse> recommendPartExercise(UserDTO userDTO, PartExerciseRequest partexerciseRequest) {

    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

    if (partexerciseRequest.getUserInput().isBlank()) {
      throw new IllegalArgumentException("USER_INPUT_IS_BLANK");
    }

    List<ExerciseRecommendation> exerciseList = chatClient.prompt()
        .options(ChatOptions.builder()
            .temperature(1.8)
            .topP(0.9)
            .build())
        .system("""
            당신은 운동만 추천하는 AI 비서입니다. 사용자가 운동과 관련 없는 질문을 한다면 정중하게 거절하세요.
            운동 6개를 사용자가 원하는 난이도로 추천해주고, 각 운동마다 관련된 youtube 링크를 해당 운동 검색 창 까지만 추가해주세요.
            사용자가 선택한 부위(ExercisePart)로 운동을 추천해주세요.
            각 운동마다 간단한 설명을 해주세요.
            
            반드시 아래 JSON 형식으로만 응답하세요.
            {
              "name": "운동 이름",
              "description": "운동 설명",
              "youtubeLink": "유튜브 링크",
              "level": "BEGINNER", "INTERMEDIATE", "ADVANCED", 이 세개중에서 사용자 선택으로
              "part": "운동 부위",
              "sets": "세트 수",
              "reps": "횟수",
              "weight": "무게"
            }
            
            """)
        .user(partexerciseRequest.getUserInput() + "\n난이도: " + partexerciseRequest.getLevel() +
            "\n운동 부위: " + partexerciseRequest.getPart())
        .call()
        .entity(new ParameterizedTypeReference<List<ExerciseRecommendation>>() {
        });

    List<Exercise> exercises = exerciseConverter.toEntity(user, exerciseList);

    exerciseService.saveAll(exercises);
    List<ExerciseResponse> exerciseResponses = exerciseConverter.toResponses(exercises);
    return exerciseResponses;
  }

  public List<ExerciseResponse> recommendExercise(UserDTO userDTO,
      ExerciseRequest exerciseRequest) {

    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

    List<ExerciseRecommendation> exerciseList = chatClient.prompt()
        .options(ChatOptions.builder()
            .temperature(1.8)
            .topP(0.9)
            .build())
        .system("""
            당신은 운동만 추천하는 AI 비서입니다. 사용자가 운동과 관련 없는 질문을 한다면 정중하게 거절하세요.
            운동 5개를 사용자가 원하는 난이도로 추천해주고, 각 운동마다 관련된 youtube 링크를 해당 운동 검색 창 까지만 추가해주세요.
            각 운동마다 간단한 설명을 해주세요. 사용자 컨디션과 운동 시간 설정에 맞춰서 운동을 추천해주세요.
            
            반드시 아래 JSON 형식으로만 응답하세요.
            {
              "name": "운동 이름",
              "description": "운동 설명",
              "youtubeLink": "유튜브 링크", 볼 수 있는 영상 링크로 보내주세요.
              "level": "BEGINNER", "INTERMEDIATE", "ADVANCED", 난이도는 초보, 중급 정도로 추천
              "part" : 운동 부위는 아무거나 랜덤으로 추천,
              "sets": "세트 수",
              "reps": "횟수",
              "weight": "무게"
            }
            
            """)
        .user("운동 시간: " + exerciseRequest.getExerciseTime() + "컨디션: " + exerciseRequest.getCondition())
        .call()
        .entity(new ParameterizedTypeReference<List<ExerciseRecommendation>>() {
        });

    List<Exercise> exercises = exerciseConverter.toEntity(user, exerciseList);
    exerciseService.saveAll(exercises);
    List<ExerciseResponse> exerciseResponses = exerciseConverter.toResponses(exercises);

      return exerciseResponses;
  }

    public ExerciseResponse getExercise(UserDTO userDTO, Long exerciseId) {
        userService.getById(userDTO.getId())
                .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

        Exercise exercise = exerciseService.getExercise(exerciseId, ExerciseStatus.REGISTERED)
                .orElseThrow(() -> new IllegalArgumentException("운동 없음"));

        return exerciseConverter.toResponse(exercise);
    }

    public List<ExerciseResponse> getByToday(UserDTO userDTO) {
        List<Exercise> exercises =
                exerciseService.getByStatusAndToday(userDTO.getId(), ExerciseStatus.REGISTERED, LocalDate.now());

      return exerciseConverter.toResponses(exercises);
    }

    public ExerciseResponse getUnregisterExercise(UserDTO userDTO, Long exerciseId) {
        userService.getById(userDTO.getId())
                .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

        Exercise exercise = exerciseService.getExercise(exerciseId, ExerciseStatus.UNREGISTERED)
                .orElseThrow(() -> new IllegalArgumentException("운동 없음"));

        return exerciseConverter.toResponse(exercise);
    }
}
