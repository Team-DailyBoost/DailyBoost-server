package com.mirae.DailyBoost.common.converter;

import com.mirae.DailyBoost.common.annotation.Converter;
import com.mirae.DailyBoost.common.model.MessageResponse;
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
