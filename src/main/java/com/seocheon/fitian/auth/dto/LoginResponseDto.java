package com.seocheon.fitian.auth.dto;

import com.seocheon.fitian.model.MemberModel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {

	private JwtTokenPair jwtTokenPair;
	private String FirebaseCustomToken;
	private MemberModel member;
	private boolean isNewMember;
}
