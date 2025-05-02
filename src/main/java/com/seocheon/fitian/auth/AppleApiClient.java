package com.seocheon.fitian.auth;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.seocheon.fitian.auth.dto.DefaultOAuthUserInfo;
import com.seocheon.fitian.auth.dto.OAuthUserInfo;

@Component
public class AppleApiClient {
	
	public OAuthUserInfo getUserInfo(String identityToken) {
		DecodedJWT jwt = JWT.decode(identityToken);
		
		String id = jwt.getSubject();
		String email = jwt.getClaim("email").asString();
		
		return new DefaultOAuthUserInfo(id, null, email);
	}
}
