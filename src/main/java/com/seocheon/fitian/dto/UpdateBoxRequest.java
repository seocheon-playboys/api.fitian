package com.seocheon.fitian.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "박스 정보 수정 요청 DTO")
public class UpdateBoxRequest {
	
	@Schema(description = "박스 연락처")
	private String boxContact;
	
	@Schema(description = "박스 주소")
	private String boxAddress;
	
	@Schema(description = "박스 설명")
	private String boxScript;
	
	@Schema(description = "박스 인스타")
	private String boxInsta;
	
	@Schema(description = "박스 요금표 이미지 url")
	private String boxFeeUrl;
	
	@Schema(description = "박스 시간표 이미지 url")
	private String boxTimeTableUrl;
}
