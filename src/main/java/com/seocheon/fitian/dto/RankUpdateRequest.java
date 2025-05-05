package com.seocheon.fitian.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원 등급 수정 요청 DTO")
public class RankUpdateRequest {

	@Schema(description = "수정할 회원의 uid")
	private String uid;
	@Schema(description = "수정할 회원의 등급", example = "guest 또는 member 또는 manager")
	private String rank;
}
