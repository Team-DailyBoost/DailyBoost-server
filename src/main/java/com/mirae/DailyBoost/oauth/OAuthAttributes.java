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
      return ofNaver(registrationId, userNameAttributeName, attributeMap);
    }

    return ofGoogle(registrationId, userNameAttributeName, attributeMap);
  }

  private static OAuthAttributes ofGoogle(String registrationId, String userNameAttributeName,
      Map<String, Object> attributeMap) {

    return OAuthAttributes.builder()
        .name((String) attributeMap.get("name"))
        .nickname((String) attributeMap.get("name"))
        .email((String) attributeMap.get("email"))
        .imageUrl((String) attributeMap.get("picture"))
        .attributeMap(attributeMap)
        .provider(registrationId)
        .providerId((String) attributeMap.get("sub"))
        .nameAttributeKey(userNameAttributeName)
        .build();
  }

  private static OAuthAttributes ofNaver(String registrationId, String userNameAttributeName,
      Map<String,Object> attributeMap) {

    Map<String, Object> response = (Map<String, Object>) attributeMap.get("response");

    String gender = (String)response.get("gender");

    if(gender == "F") {
      gender = "FEMALE";
    } else {
      gender = "MALE";
    }

    LocalDate birth = getBirthDay(response);
    int ageYears = AgeUtil.calculateAgeAt(birth.toString(), LocalDate.now(ZoneId.of("Asia/Seoul")));

    return OAuthAttributes.builder()
        .email((String)response.get("email"))
        .name((String)response.get("name"))
        .nickname((String)response.get("nickname"))
        .imageUrl((String)response.get("profile_image"))
        .gender(Gender.valueOf(gender))
        .birthDay(getBirthDay(response))
        .age(String.valueOf(ageYears))
        .provider(registrationId)
        .providerId((String) response.get("id"))
        .nameAttributeKey(userNameAttributeName)
        .attributeMap(attributeMap)
        .build();

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
        .gender(Gender.valueOf(((String) kakaoAccount.get("gender")).toUpperCase()))
        .birthDay(getBirthDay(kakaoAccount))
        .age(String.valueOf(ageYears))
        .provider(registrationId)
        .providerId(String.valueOf(attributeMap.get("id")))
        .nameAttributeKey(userNameAttributeName)
        .attributeMap(attributeMap)
        .build();
  }

  private static LocalDate getBirthDay(Map<String, Object> account) {
    String birthYear = (String) account.get("birthyear");
    String birthDay = (String) account.get("birthday");

    if (birthDay.contains("-")) {
      birthDay = birthDay.replace("-", "");
    }

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
