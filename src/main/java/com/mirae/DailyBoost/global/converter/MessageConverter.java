package com.mirae.DailyBoost.global.converter;

import com.mirae.DailyBoost.global.annotation.Converter;
import com.mirae.DailyBoost.global.model.MessageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Converter
@Builder
@AllArgsConstructor
public class MessageConverter {

  public MessageResponse toResponse(String message) {
    return MessageResponse.builder()
        .message(message)
        .build();
  }

}
