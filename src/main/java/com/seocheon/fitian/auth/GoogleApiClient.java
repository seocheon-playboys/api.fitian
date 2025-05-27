package com.seocheon.fitian.auth;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.seocheon.fitian.auth.dto.DefaultOAuthUserInfo;
import com.seocheon.fitian.auth.dto.GoogleUserResponse;
import com.seocheon.fitian.auth.dto.OAuthUserInfo;

@Component
public class GoogleApiClient {
	private final RestTemplate restTemplate = new RestTemplate();
	
	public OAuthUserInfo getUserInfo(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		
		HttpEntity<?> entity = new HttpEntity<>(headers);
		
		ResponseEntity<GoogleUserResponse> response = restTemplate.exchange(
				"https://openidconnect.googleapis.com/v1/userinfo",
	            HttpMethod.GET,
	            entity,
	            GoogleUserResponse.class
		);
		
		GoogleUserResponse body = response.getBody();
		
		String id = body.getSub();
		String nickname = body.getName();
		String email = body.getEmail();
		
		return new DefaultOAuthUserInfo(id, nickname, email);
	}
}
