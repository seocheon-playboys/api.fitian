package com.seocheon.fitian.auth.dto;

import lombok.Data;

@Data
public class GoogleUserResponse {
	private String uid;
	private String name;
	private String email;
}
