package com.mirae.DailyBoost.openChatAI.business;

import com.mirae.DailyBoost.global.annotation.Business;
import com.mirae.DailyBoost.exercise.domain.controller.model.request.ExerciseRecommendation;
import com.mirae.DailyBoost.openChatAI.controller.model.request.ExerciseRequest;
import com.mirae.DailyBoost.food.domain.controller.model.response.FoodRecommendation;
import com.mirae.DailyBoost.openChatAI.controller.model.request.FoodRequest;
import com.mirae.DailyBoost.food.domain.controller.model.request.RecipeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;

@Business
@RequiredArgsConstructor
public class GeminiBusiness {

  @Qualifier("geminiClient")
  private final ChatClient chatClient;

  public ExerciseRecommendation recommendExercise(ExerciseRequest exerciseRequest) {

    if (exerciseRequest.getUserInput().isBlank()) {
      throw new IllegalArgumentException("USER_INPUT_IS_BLANK");
    }

    return chatClient.prompt()
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
        .user(exerciseRequest.getUserInput() + "\n난이도: " + exerciseRequest.getLevel())
        .call()
        .entity(ExerciseRecommendation.class);
  }

  public FoodRecommendation recommendFood(FoodRequest foodRequest) {
    String healthInfo = "키: 173, 몸무게: 70, 활동량: 집에서 빈둥거림, 목표: 멋있는 몸매 만들기"; // 삭제 예정
    String allergy = "복숭아, 땅콩"; // 삭제 예정


    if (foodRequest.getUserInput().isBlank()) {
      throw new IllegalArgumentException("USER_INPUT_IS_BLANK");
    }

    return chatClient.prompt()
        .system("""
            당신은 음식만 추천해주는 AI비서입니다. 사용자가 음식과 관련 없는 질문을 한다면 정중하게 거절하세요.
            사용자의 프로필 정보(키, 몸무게, 활동량, 목표)를 기반으로 아침, 점심, 저녁 메뉴를 추천해주세요.
            사용자가 알레르기가 있을 경우 알레르기 유발 재료가 들어가는 메뉴는 제외해주세요.
            
            반드시 아래 JSON 형식으로만 응답하세요.
            {
              "foodInfoDto": [
                {
                  "name": "음식 이름",
                  "calory": "음식 칼로리 함유량",
                  "carbohydrate": "음식 탄수화물 함유량"
                  "protein": "음식 단백질 함유량"
                  "fat": "음식 자방 함유량"
                  "foodKind": BREAKFAST("아침"), LUNCH("점심"), DINNER("저녁")
                  "description": "음식 설명"
                }
              ]
            }
            
            """)
        .user(foodRequest.getUserInput() + "/n 바디프로필 정보:" + healthInfo + "/n 알레르기:" + allergy)
        .call()
        .entity(FoodRecommendation.class);

  }

  public FoodRecommendation recommendRecipe(RecipeRequest recipeRequest) {

    if (recipeRequest.getUserInput().isBlank()) {
      throw new IllegalArgumentException("USER_INPUT_IS_BLANK");
    }

    return chatClient.prompt()
        .system("""
            당신은 음식 레시피만 추천해주는 AI비서입니다. 사용자가 레시피와 관련 없는 질문을 한다면 정중하게 거절하세요.
            사용자가 가지고 있는 재료들로 레시피를 추천해주세요. 재료가 한 두가지가 없어도 추천해도 됨.
            추천한 음식을 어떻게 만드는지 자세히 설명해주세요.
            메뉴 하나만 추천해주세요. 
            
            반드시 아래 JSON 형식으로만 응답하세요.
            {
              "foodInfoDto": [
              {
                  "name": "음식 이름",
                  "calory": "음식 칼로리 함유량",
                  "carbohydrate": "음식 탄수화물 함유량"
                  "protein": "음식 단백질 함유량"
                  "fat": "음식 자방 함유량"
                  "foodKind": "RECIPE"
                  "description": 음식 만드는 방법 자세히 (설명)
                  }
                ]
            }
            """)
        .user(recipeRequest.getUserInput())
        .call()
        .entity(FoodRecommendation.class);
  }
}
