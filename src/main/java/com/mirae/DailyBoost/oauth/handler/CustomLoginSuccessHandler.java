package com.mirae.DailyBoost.oauth.handler;

import com.mirae.DailyBoost.global.errorCode.UserErrorCode;
import com.mirae.DailyBoost.jwt.business.JwtBusiness;
import com.mirae.DailyBoost.oauth.CustomOAuth2User;
import com.mirae.DailyBoost.user.domain.repository.User;
import com.mirae.DailyBoost.user.domain.service.UserService;
import com.mirae.DailyBoost.user.exception.user.UserNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtBusiness jwtBusiness;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            String accessToken = jwtBusiness.createAccessToken(oAuth2User.getEmail(), oAuth2User.getName(), oAuth2User.getUser()
                    .getRole());
            String refreshToken = jwtBusiness.createRefreshToken();
            response.addHeader(jwtBusiness.getAccessHeader(), "Bearer " + accessToken);
            response.addHeader(jwtBusiness.getRefreshHeader(), "Bearer " + refreshToken);

            jwtBusiness.sendAccessAndRefreshToken(response, accessToken, refreshToken);
            jwtBusiness.updateRefreshToken(oAuth2User.getEmail(), refreshToken);

            User user = userService.getByEmail(oAuth2User.getEmail())
                    .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));
            user.initLastLoginAt(LocalDateTime.now());
            log.info("로그인 성공 =================== {} ================", oAuth2User.getEmail());
            userService.save(user);

            String provider = resolveProvider(request.getRequestURI());
            String redirectUri = buildMobileRedirectUri(provider, accessToken, refreshToken);

            sendMobileRedirectPage(response, redirectUri, accessToken, refreshToken);
            return;

        } catch (Exception e) {
            throw e;
        }

    }

    private String resolveProvider(String requestUri) {
        if (requestUri.contains("naver")) {
            return "naver";
        }
        if (requestUri.contains("kakao")) {
            return "kakao";
        } else {
            return "google";
        }
    }

    private String buildMobileRedirectUri(String provider, String accessToken, String refreshToken) {
            String encodedAccess = URLEncoder.encode(accessToken, StandardCharsets.UTF_8);
            String encodedRefresh = URLEncoder.encode(refreshToken, StandardCharsets.UTF_8);
            return new StringBuilder("dailyboost://oauth/")
                    .append(provider)
                    .append("?access=")
                    .append(encodedAccess)
                    .append("&refresh=")
                    .append(encodedRefresh)
                    .toString();
        }

        private void sendMobileRedirectPage(HttpServletResponse response, String redirectUri,
                String accessToken, String refreshToken) throws IOException {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("X-Frame-Options", "ALLOWALL");
            response.setHeader("Cache-Control", "no-store, max-age=0");
            response.setContentType("text/html;charset=UTF-8");

            String html = """
        <!DOCTYPE html>
        <html lang="ko">
        <head>
          <meta charset="UTF-8" />
          <title>OAuth Redirect</title>
          <meta http-equiv="X-UA-Compatible" content="IE=edge" />
          <meta name="viewport" content="width=device-width, initial-scale=1.0" />
          <style>
            body { font-family: sans-serif; padding: 32px; background: #f9fafb; color: #111827; }
            h1 { font-size: 20px; margin-bottom: 12px; }
            p { line-height: 1.6; }
            button { margin-top: 24px; padding: 12px 18px; background: #6366f1; color: #fff; border: none; border-radius: 8px; font-size: 16px; }
            .info { margin-top: 28px; padding: 16px; background: #fff; border-radius: 12px; box-shadow: 0 0 0 1px rgba(99,102,241,0.12); }
            code { display: block; margin-top: 8px; word-break: break-all; background: #eef2ff; padding: 8px; border-radius: 6px; }
          </style>
        </head>
        <body>
          <h1>로그인을 완료했어요.</h1>
          <p>잠시 후 앱으로 이동합니다. 자동으로 이동하지 않으면 아래 버튼을 눌러주세요.</p>
          <button id="openAppBtn">앱으로 돌아가기</button>
          <div class="info">
            <strong>수신한 토큰 정보</strong>
            <code id="accessToken"></code>
            <code id="refreshToken"></code>
          </div>
          <script>
            (function() {
              var redirectUri = '%s';
              var access = '%s';
              var refresh = '%s';

              document.getElementById('accessToken').textContent = 'access=' + access.substring(0, 50) + '...';
              document.getElementById('refreshToken').textContent = 'refresh=' + refresh.substring(0, 50) + '...';

              function openApp() {
                window.location.href = redirectUri;
              }

              document.getElementById('openAppBtn').addEventListener('click', openApp);

              if (window.ReactNativeWebView && window.ReactNativeWebView.postMessage) {
                try {
                  window.ReactNativeWebView.postMessage(JSON.stringify({
                    type: 'token',
                    jwtToken: access,
                    refreshToken: refresh
                  }));
                } catch (e) {}
              }

              setTimeout(openApp, 400);
            })();
          </script>
        </body>
        </html>
        """.formatted(redirectUri, accessToken, refreshToken);

            response.getWriter().write(html);
            response.getWriter().flush();
        }
    }
