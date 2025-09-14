package com.mirae.DailyBoost.openChatAI.business;

import com.mirae.DailyBoost.common.annotation.Business;
import com.mirae.DailyBoost.common.api.Api;
import com.mirae.DailyBoost.exercise.domain.controller.model.request.ExerciseRecommendation;
import com.mirae.DailyBoost.exercise.domain.service.ExerciseService;
import com.mirae.DailyBoost.openChatAI.controller.model.request.UserInputRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;

@Business
@RequiredArgsConstructor
public class GeminiBusiness {

  @Qualifier("geminiClient")
  private final ChatClient chatClient;

  public Api<ExerciseRecommendation> recommendExercise(UserInputRequest userInputRequest) {

    if (userInputRequest.getUserInput().isBlank()) {
      throw new IllegalArgumentException("USER_INPUT_IS_BLANK");
    }

    ExerciseRecommendation exerciseRecommendation = chatClient.prompt()
        .system("""
            당신은 운동만 추천하는 AI 비서입니다. 사용자가 운동과 관련 없는 질문을 한다면 정중하게 거절하세요.
            운동 5개를 사용자가 원하는 난이도로 추천해주고, 각 운동마다 관련된 youtube 링크를 추가해주세요.
            각 운동마다 간단한 설명을 해주세요.
            
            반드시 아래 JSON 형식으로만 응답하세요.
            {
              "exerciseInfoDto": [
                {
                  "name": "운동 이름",
                  "description": "운동 설명",
                  "youtubeLink": "유튜브 링크"
                  "level": "BEGINNER", "INTERMEDIATE", "ADVANCED" 이 세개중에서 사용자 선택으로
                }
              ]
            }
            
            """)
        .user(userInputRequest.getUserInput() + "\n난이도: " + userInputRequest.getLevel())
        .call()
        .entity(ExerciseRecommendation.class);
    return Api.OK(exerciseRecommendation);


  }
}
