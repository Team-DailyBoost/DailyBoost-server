package com.mirae.DailyBoost.oauth;

import com.mirae.DailyBoost.user.domain.repository.enums.Gender;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OAuthAttributes {

  private Map<String, Object> attributeMap;
  private String nameAttributeKey;
  private String email;
  private String name;
  private String nickname;
  private String imageUrl;
  private String phone;
  private Gender gender;
  private LocalDate birthDay;
  private String age;
  private String provider;
  private String providerId;

  public static OAuthAttributes of(String registrationId, String userNameAttributeName,
      Map<String, Object> attributeMap) {
    if ("kakao".equals(registrationId)) {
      return ofKakao(registrationId, userNameAttributeName, attributeMap);
    } else if ("naver".equals(registrationId)) {
      return null; // 구현 예정
    }

    return ofGoogle(registrationId, userNameAttributeName, attributeMap); // 구현 예정
  }

  private static OAuthAttributes ofGoogle(String registrationId, String userNameAttributeName,
      Map<String, Object> attributeMap) {

    return null;
  }

  private static OAuthAttributes ofKakao(String registrationId, String userNameAttributeName,
      Map<String, Object> attributeMap) {
    Map<String, Object> kakaoAccount = (Map<String, Object>) attributeMap.get("kakao_account");
    Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

    LocalDate birth = getBirthDay(kakaoAccount);
    int ageYears = AgeUtil.calculateAgeAt(birth.toString(), LocalDate.now(ZoneId.of("Asia/Seoul")));

    return OAuthAttributes.builder()
        .email((String) kakaoAccount.get("email"))
        .name((String) kakaoAccount.get("name"))
        .nickname((String) profile.get("nickname"))
        .imageUrl((String) profile.get("profile_image_url"))
        .phone((String) kakaoAccount.get("phone_number"))
        .gender(Gender.valueOf(((String) kakaoAccount.get("gender")).toUpperCase()))
        .birthDay(getBirthDay(kakaoAccount))
        .age(String.valueOf(ageYears))
        .provider(registrationId)
        .providerId(String.valueOf(attributeMap.get("id")))
        .nameAttributeKey(userNameAttributeName)
        .attributeMap(attributeMap)
        .build();
  }

  private static LocalDate getBirthDay(Map<String, Object> kakaoAccount) {
    String birthYear = (String) kakaoAccount.get("birthyear");
    String birthDay = (String) kakaoAccount.get("birthday");

    return LocalDate.parse(birthYear + birthDay, DateTimeFormatter.BASIC_ISO_DATE);

  }

  public static class AgeUtil {

    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    /**
     * 오늘(KST 기준) 기준 만 나이 계산
     */
    public static int calculateAge(String birthDateIso) {
      return calculateAgeAt(birthDateIso, LocalDate.now(KST));
    }

    public static int calculateAgeAt(String birthDateIso, LocalDate asOfDate) {
      if (birthDateIso == null || birthDateIso.isBlank()) {
        throw new IllegalArgumentException(
            "birthDateIso must not be null/blank (expected yyyy-MM-dd)");
      }
      try {
        LocalDate birth = LocalDate.parse(birthDateIso, ISO_DATE);
        if (birth.isAfter(asOfDate)) {
          throw new IllegalArgumentException("Birthdate is in the future.");
        }
        return Period.between(birth, asOfDate).getYears();
      } catch (DateTimeParseException e) {
        throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd (e.g., 1995-10-03)",
            e);
      }
    }
  }
}
