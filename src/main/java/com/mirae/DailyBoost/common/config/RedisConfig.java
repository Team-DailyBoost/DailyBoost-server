package com.mirae.DailyBoost.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Value("${spring.data.redis.host}")
  private String host;

  @Value("${spring.data.redis.port}")
  private int port;

  /**
   * Redis 연결을 위한 Connection 생성
   *
   * @return RedisConnectionFactory
   */
  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(host, port);
  }


  /**
   * Redis 데이터 처리를 위한 템플릿 구성
   * 해당 구성된 RedisTemplate을 통해서 데이터 통신 처리에 대한 직력화를 수행
   *
   * @return RedisTemplate
   */
  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

    redisTemplate.setConnectionFactory(redisConnectionFactory());

    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());

    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(new StringRedisSerializer());

    redisTemplate.setDefaultSerializer(new StringRedisSerializer());

    return redisTemplate;
  }

  /**
   * 리스트에 접근하여 다양한 연산을 수행합니다.
   *
   * @return ListOperations
   */
  public ListOperations<String, Object> getListOperations() {
    return this.redisTemplate().opsForList();
  }

  /**
   * 단일 데이터에 접근하여 다양한 연산을 수행합니다.
   *
   * @return ValueOperations
   */
  public ValueOperations<String, Object> getValueOperations() {
    return this.redisTemplate().opsForValue();
  }

  /**
   * Redis 작업 중 등록, 수정, 삭제에 대해서 처리 및 예외처리를 수행
   *
   * @param operation
   * @return int
   */
  public int executeoperation(Runnable operation) {
    try {
      operation.run();
      return 1;
    } catch (Exception e) {
      System.out.println("Redis 작업 오류 발생 :: " + e.getMessage());
      return 0;
    }
  }


}
