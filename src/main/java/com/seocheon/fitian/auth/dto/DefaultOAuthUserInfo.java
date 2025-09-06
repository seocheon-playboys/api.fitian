package com.seocheon.fitian.auth.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class DefaultOAuthUserInfo implements OAuthUserInfo {

	private String id;
	private String nickname;
	private String email;

}
