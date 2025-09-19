package com.mirae.DailyBoost.oauth;

import com.mirae.DailyBoost.user.domain.repository.enums.Gender;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
  private String phone;
  private Gender gender;
  private LocalDate birthDay;
  private String provider;
  private String providerId;

  public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributeMap) {
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

  private static OAuthAttributes ofKakao(String registrationId, String userNameAttributeName, Map<String, Object> attributeMap) {
    Map<String, Object> kakaoAccount = (Map<String, Object>) attributeMap.get("kakao_account");
    Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

    return OAuthAttributes.builder()
        .email((String) kakaoAccount.get("email"))
        .name((String) kakaoAccount.get("name"))
        .nickname((String) profile.get("nickname"))
        .phone((String) kakaoAccount.get("phone_number"))
        .gender(Gender.valueOf(((String) kakaoAccount.get("gender")).toUpperCase()))
        .birthDay(getBirthDay(kakaoAccount))
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

}
