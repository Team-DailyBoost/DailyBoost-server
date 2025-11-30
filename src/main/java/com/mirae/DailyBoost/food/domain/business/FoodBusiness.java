package com.mirae.DailyBoost.food.domain.business;

import com.mirae.DailyBoost.global.annotation.Business;
import com.mirae.DailyBoost.global.converter.MessageConverter;
import com.mirae.DailyBoost.global.model.MessageResponse;
import com.mirae.DailyBoost.food.domain.controller.model.response.FoodResponse;
import com.mirae.DailyBoost.food.domain.converter.FoodConverter;
import com.mirae.DailyBoost.food.domain.repository.Food;
import com.mirae.DailyBoost.food.domain.repository.enums.FoodStatus;
import com.mirae.DailyBoost.food.domain.service.FoodService;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.food.domain.controller.model.response.FoodRecommendation;
import com.mirae.DailyBoost.food.domain.controller.model.request.RecipeRequest;
import com.mirae.DailyBoost.user.domain.repository.HealthInfo;
import com.mirae.DailyBoost.user.domain.repository.User;
import com.mirae.DailyBoost.user.domain.service.UserService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;

@Business
@Slf4j
@RequiredArgsConstructor
public class FoodBusiness {

  private final FoodService foodService;
  private final FoodConverter foodConverter;
  private final UserService userService;
  private final MessageConverter messageConverter;

  @Qualifier("geminiClient")
  private final ChatClient chatClient;

  public MessageResponse register(UserDTO userDTO, Long foodId) {

    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    Food food = foodService.getById(foodId)
        .orElseThrow(() -> new IllegalArgumentException("FOOD_NOT_FOUND"));

    if(food.getUser().getId() != user.getId()) {
      throw new IllegalArgumentException("등록할 권한이 없습니다.");
    }

    food.changeStatus(FoodStatus.REGISTERED);
    food.initDate(LocalDate.now());

    foodService.save(food);

    return messageConverter.toResponse("식단이 추가되었습니다.");
  }


  public MessageResponse unregister(UserDTO userDTO, Long foodId) {
    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    Food food = foodService.getById(foodId)
        .orElseThrow(() -> new IllegalArgumentException("FOOD_NOT_FOUND"));

    if(food.getUser().getId() != user.getId()) {
      throw new IllegalArgumentException("삭제할 권한이 없습니다.");
    }

    food.changeStatus(FoodStatus.UNREGISTERED);

    foodService.save(food);

    return messageConverter.toResponse("식단이 제거되었습니다.");

  }

  public List<FoodResponse> recommendFood(UserDTO userDTO) {

    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    HealthInfo healthInfo = user.getHealthInfo();

    List<FoodRecommendation> foodList = chatClient.prompt()
            .options(ChatOptions.builder()
                    .temperature(1.8)
                    .topP(0.9)
                    .build())
        .system("""
            당신은 음식만 추천해주는 AI비서입니다. 사용자가 음식과 관련 없는 질문을 한다면 정중하게 거절하세요.
            사용자의 프로필 정보(키, 몸무게, 목표)를 기반으로 아침, 점심, 저녁 메뉴를 추천해주세요.
            추천 시 매번 무작위 요소를 포함하여 서로 다른 답변을 생성하세요.
            이전 응답과 동일한 음식 조합은 피하세요.
            
            사용자 목표 (Goal)
              WEIGHT_LOSS,                // 체중 감량
              MUSCLE_GAIN,                // 근육 증가
              STRENGTH_IMPROVEMENT,       // 근력 향상
              ENDURANCE_IMPROVEMENT,      // 지구력 향상
              GENERAL_HEALTH_MAINTENANCE, // 일반 건강 유지
              BODY_SHAPE_MANAGEMENT       // 체형 관리
            
            반드시 아래 JSON 형식으로만 응답하세요.
            {
                  "name": "음식 이름",
                  "calory": "음식 칼로리 함유량",
                  "carbohydrate": "음식 탄수화물 함유량"
                  "protein": "음식 단백질 함유량"
                  "fat": "음식 자방 함유량"
                  "foodKind": BREAKFAST("아침"), LUNCH("점심"), DINNER("저녁")
                  "description": "음식 설명",
                  "weight": "음식 그램수 g"
            }
            """)
        .user("\n 키: " + healthInfo.getHeight() + "\n 몸무게: " + healthInfo.getWeight() +
            "\n 목표: " + healthInfo.getGoal())
        .call()
        .entity(new ParameterizedTypeReference<List<FoodRecommendation>>() {
        });

    List<Food> foods = foodConverter.toEntity(user, foodList);
    foodService.saveAll(foods);

      List<FoodResponse> foodResponses = foodConverter.toResponses(foods);


      return foodResponses;
  }


  public FoodResponse recommendRecipe(UserDTO userDTO, RecipeRequest recipeRequest) {

    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    if (recipeRequest.getUserInput().isBlank()) {
      throw new IllegalArgumentException("USER_INPUT_IS_BLANK");
    }

    FoodRecommendation foodRecommendation = chatClient.prompt()
        .system("""
            당신은 음식 레시피만 추천해주는 AI비서입니다.
            사용자가 가지고 있는 재료들로 레시피를 추천해주세요. 재료가 한 두가지가 없어도 추천해도 됨.
            추천한 음식을 어떻게 만드는지 자세히 설명해주세요.
            메뉴 하나만 추천해주세요. 
            
            반드시 아래 JSON 형식으로만 응답하세요.
            {
                  "name": "음식 이름",
                  "calory": "음식 칼로리 함유량",
                  "carbohydrate": "음식 탄수화물 함유량"
                  "protein": "음식 단백질 함유량"
                  "fat": "음식 자방 함유량"
                  "foodKind": "RECIPE"
                  "description": 음식 만드는 방법 자세히 (설명),
                  "weight": 음식 그램 수 g 
            }
            
            사용자가 레시피와 관련 없는 질문을 한다면 null값을 넣어주세요.
            {
                  "name": null,
                  "calory": null,
                  "carbohydrate": null
                  "protein": null
                  "fat": null
                  "foodKind": null
                  "description": null,
                  "weight": null
            }
            """)
        .user(recipeRequest.getUserInput())
        .call()
        .entity(FoodRecommendation.class);

    if(foodRecommendation.getName() == null) {
      throw new IllegalArgumentException("레시피와 관련없는 질문입니다.");
    }

    Food food = foodConverter.toRecipeFoodEntity(user, foodRecommendation);
    foodService.save(food);
      FoodResponse foodResponse = foodConverter.toResponse(food);

      return foodResponse;
  }

  public List<FoodResponse> getByKeyword(UserDTO userDTO, String keyword) {
    List<Food> foods = foodService.getByKeywordAndStatus(userDTO.getId(), keyword, FoodStatus.UNREGISTERED);

    if(foods.isEmpty()) {
      throw new IllegalArgumentException("FOOD_NOT_FOUND");
    }

    return foodConverter.toResponses(foods);

  }

  public List<FoodResponse> getByToday(UserDTO userDTO) {
    List<Food> foods = foodService.getByStatusAndToday(userDTO.getId(), FoodStatus.REGISTERED, LocalDate.now());

    if(foods.isEmpty()) {
      throw new IllegalArgumentException("FOOD_NOT_FOUND");
    }

    return foodConverter.toResponses(foods);
  }

  public List<FoodResponse> getByWeekly(UserDTO userDTO) {
    List<Food> foods = foodService.getByStatusAndWeekly(userDTO.getId(), FoodStatus.REGISTERED);

    if(foods.isEmpty()) {
      throw new IllegalArgumentException("FOOD_NOT_FOUND");
    }

    return foodConverter.toResponses(foods);
  }

  public MessageResponse reset(UserDTO userDTO) {
    List<Food> foods = foodService.getByStatusAndToday(userDTO.getId(),
        FoodStatus.REGISTERED, LocalDate.now());

    if(foods.isEmpty()) {
      throw new IllegalArgumentException("FOOD_NOT_FOUND");
    }

    foods.stream().forEach(food -> {
      food.changeStatus(FoodStatus.UNREGISTERED);
    });

    foodService.saveAll(foods);

    return messageConverter.toResponse("오늘 식단이 초기화 되었습니다.");
  }
}
