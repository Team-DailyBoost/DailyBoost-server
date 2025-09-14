package com.mirae.DailyBoost.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ChatClientConfig {

  @Bean
  @Primary
  public ChatClient geminiClient(@Qualifier("vertexAiGeminiChat") ChatModel chatModel) {
    return ChatClient.builder(chatModel)
        .defaultSystem("당신은 사용자의 비서입니다. 하지만 비속어나 주제에 관련되지 않는 질문은 정중하게 거절하세요.")
        .build();
  }
}
