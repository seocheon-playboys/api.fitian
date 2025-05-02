package com.seocheon.fitian.auth.dto;

import lombok.Data;

@Data
public class AppleIdTokenPayload {
	private String sub;
	private String email;
	private String email_verified;
	private long auth_time;
}
