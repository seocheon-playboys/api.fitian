package com.seocheon.fitian.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원 정보 수정 요청 DTO")
public class UpdateSelfRequest {

	@Schema(description = "회원 이름")
	private String name;
	
	@Schema(description = "회원 이메일")
	private String email;
	
	@Schema(description = "가입 추가 정보 입력시 박스 이름")
	private String boxName;
	
	@Schema(description = "가입 추가 정보 입력시 박스 코드")
	private String boxCode;
	
	@Schema(description = "가입 추가 정보 입력시 성별")
	private String gender;
}
