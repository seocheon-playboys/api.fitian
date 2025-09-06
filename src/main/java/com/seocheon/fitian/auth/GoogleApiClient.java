package com.seocheon.fitian.auth;

import java.util.Collections;
import java.util.Map;

import com.seocheon.fitian.auth.dto.OAuthUserInfo;
import com.seocheon.fitian.auth.dto.DefaultOAuthUserInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Google token/code 처리 유틸리티
 *  - 먼저 access_token으로 userinfo 호출 시도
 *  - 실패하면 전달값을 authorization code로 간주하고 token 교환 → id_token/ access_token 사용
 */
@Component
public class GoogleApiClient {
	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${google.web.client-id}")
	private String webClientId;

	@Value("${google.web.client-secret}")
	private String webClientSecret;

	public OAuthUserInfo getUserInfo(String tokenOrCode) {
		// 1) access_token으로 userinfo 호출 시도
		try {
			Map<String, Object> userInfo = fetchUserInfoByAccessToken(tokenOrCode);
			if (userInfo != null) {
				return mapToOAuthUserInfo(userInfo);
			}
		} catch (HttpClientErrorException e) {
			// 401/400 등: access_token invalid -> code 교환 시도
		} catch (Exception e) {
			// 기타 예외: 계속 below로 내려감
		}

		// 2) tokenOrCode를 authorization code로 보고 교환 시도
		Map<String, Object> tokenResponse = exchangeAuthCodeForToken(tokenOrCode);
		if (tokenResponse == null) {
			throw new RuntimeException("Google token exchange failed");
		}

		// 3) id_token이 있으면 JWT 검증 후 payload 기반 사용자정보 사용
		if (tokenResponse.containsKey("id_token")) {
			String idToken = tokenResponse.get("id_token").toString();
			OAuthUserInfo info = verifyIdTokenAndMap(idToken);
			if (info != null) return info;
		}

		// 4) id_token이 없거나 검증 실패하면 access_token으로 userinfo 조회
		Object access = tokenResponse.get("access_token");
		if (access == null) {
			throw new RuntimeException("No access_token or id_token from Google token exchange");
		}
		Map<String, Object> userInfo = fetchUserInfoByAccessToken(access.toString());
		if (userInfo == null) {
			throw new RuntimeException("Failed to get userinfo after token exchange");
		}
		return mapToOAuthUserInfo(userInfo);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> fetchUserInfoByAccessToken(String accessToken) {
		String url = "https://www.googleapis.com/oauth2/v3/userinfo";
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
		return resp.getBody();
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> exchangeAuthCodeForToken(String code) {
		String url = "https://oauth2.googleapis.com/token";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("code", code);
		form.add("client_id", webClientId);
		form.add("client_secret", webClientSecret);
		form.add("grant_type", "authorization_code");
		// 만약 Google Console에 redirect_uri가 등록되어 있고 교환 시 필요하면 추가하세요:
		// form.add("redirect_uri", "<YOUR_REGISTERED_REDIRECT_URI>");

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);
		ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
		return resp.getBody();
	}

	private OAuthUserInfo verifyIdTokenAndMap(String idTokenStr) {
		try {
			GsonFactory gsonFactory = new GsonFactory();
			NetHttpTransport transport = new NetHttpTransport();
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, gsonFactory)
					.setAudience(Collections.singletonList(webClientId))
					.build();

			GoogleIdToken idToken = GoogleIdToken.parse(gsonFactory, idTokenStr);
			if (!verifier.verify(idToken)) {
				throw new RuntimeException("Invalid ID token signature");
			}
			Payload payload = idToken.getPayload();
			String sub = payload.getSubject(); // 고유한 google id
			String email = payload.getEmail();
			String name = (String) payload.get("name");
			Boolean emailVerified = Boolean.TRUE.equals(payload.getEmailVerified());

			DefaultOAuthUserInfo info = new DefaultOAuthUserInfo();
			info.setId(sub);
			info.setEmail(email);
			info.setNickname(name);
			return info;
		} catch (Exception e) {
			throw new RuntimeException("Failed to verify id_token", e);
		}
	}

	@SuppressWarnings("unchecked")
	private OAuthUserInfo mapToOAuthUserInfo(Map<String, Object> userInfo) {
		String sub = userInfo.get("sub") != null ? userInfo.get("sub").toString()
				: (userInfo.get("id") != null ? userInfo.get("id").toString() : null);
		String email = userInfo.get("email") != null ? userInfo.get("email").toString() : null;
		String name = userInfo.get("name") != null ? userInfo.get("name").toString() : null;

		DefaultOAuthUserInfo info = new DefaultOAuthUserInfo();
		info.setId(sub);
		info.setEmail(email);
		info.setNickname(name);
		return info;
	}
}
