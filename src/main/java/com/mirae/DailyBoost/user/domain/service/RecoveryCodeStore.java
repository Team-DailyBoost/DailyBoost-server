package com.mirae.DailyBoost.user.domain.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecoveryCodeStore {
  private final StringRedisTemplate redis;

  private static final String CODE_KEY = "recovery:code:"; // 계정 복구 코드
  private static final String ATTEMPTS_KEY = "recovery:attempts:"; // 틀린 입력 시도 횟수
  private static final String SEND_LIMIT_KEY = "recovery:send:lim:"; // 재발송 제한

  /**
   * 코드 저장
   *
   * @param email
   * @param code
   * @param ttl
   */
  public void saveCode(String email, String code, Duration ttl) {
    redis.opsForValue().set(CODE_KEY + email, code, ttl);

    // 새 코드 발급 시 기존 실패 횟수 리셋
    redis.delete(ATTEMPTS_KEY + email);
  }

  /**
   * 재발송 제한
   *
   * @param email
   * @return
   */
  public boolean underSendLimit(String email) {

    /**
     * true -> 발송 허용
     * 30초내 재호출 시 false 반환
      */
    Boolean ok = redis.opsForValue()
        .setIfAbsent(SEND_LIMIT_KEY + email, "1", Duration.ofSeconds(30));
    return Boolean.TRUE.equals(ok);
  }

  /**
   * 검증&소모
   *
   * @param email
   * @param inputCode
   * @return
   */
  public VerifyResult verifyAndConsume(String email, String inputCode) {
    String stored = redis.opsForValue().get(CODE_KEY + email);
    if (stored == null) return VerifyResult.EXPIRED_OR_NOT_FOUND; // 만료되거나 발급 안함
    if (stored.equals(inputCode)) { // 코드가 일치할 때 코드 키 삭제, 틀린 횟수도 삭제
      redis.delete(CODE_KEY + email);
      redis.delete(ATTEMPTS_KEY + email);
      return VerifyResult.SUCCESS; // 인증 성공
    } else {
      Long a = redis.opsForValue().increment(ATTEMPTS_KEY + email);
      if (a != null && a == 1L) redis.expire(ATTEMPTS_KEY + email, Duration.ofMinutes(5));
      return VerifyResult.MISMATCH;
    }
  }

  public enum VerifyResult { SUCCESS, MISMATCH, EXPIRED_OR_NOT_FOUND }
}

