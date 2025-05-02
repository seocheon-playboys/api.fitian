package com.seocheon.fitian.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DefaultOAuthUserInfo implements OAuthUserInfo {

	private String id;
	private String nickname;
	private String email;
}
