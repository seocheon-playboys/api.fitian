package com.seocheon.fitian.auth;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.seocheon.fitian.auth.dto.DefaultOAuthUserInfo;
import com.seocheon.fitian.auth.dto.KakaoUserResponse;
import com.seocheon.fitian.auth.dto.OAuthUserInfo;

@Component
public class KakaoApiClient {
	private final RestTemplate restTemplate = new RestTemplate();
	
	public OAuthUserInfo getUserInfo(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		
		HttpEntity<?> entity = new HttpEntity<>(headers);
		
		ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
				"https://kapi.kakao.com/v2/user/me",
	            HttpMethod.GET,
	            entity,
	            KakaoUserResponse.class
		);
		
		KakaoUserResponse body = response.getBody();
		
		String id = body.getId();
		String nickname = body.getProperties() != null ? body.getProperties().getNickname() : null;
		String email = body.getKakao_account() != null ? body.getKakao_account().getEmail() : null;
		
		return new DefaultOAuthUserInfo(id, nickname, email);
	}
}
