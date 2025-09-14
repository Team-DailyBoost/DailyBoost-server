package com.mirae.DailyBoost.openChatAI.controller.model.request;

import com.mirae.DailyBoost.exercise.domain.repository.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInputRequest {

  private String userInput;
  private Level level;

}
