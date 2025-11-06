package com.mirae.DailyBoost.jwt.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtKeyConfig {
  @Bean
  public SecretKey jwtSecretKey(@Value("${JWT_SECRET_KEY}") String secretKey) {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }
}
