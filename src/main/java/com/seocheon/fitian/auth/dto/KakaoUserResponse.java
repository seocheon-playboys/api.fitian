package com.seocheon.fitian.auth.dto;

import lombok.Data;

@Data
public class KakaoUserResponse {
	private String id;
	private KakaoProperties properties;
	private KaKaoAccount kakao_account;
}
