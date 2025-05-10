package com.seocheon.fitian.dto;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "박스 정보 이미지 수정 요청 DTO")
public class UpdateBoxInfoImageRequest {
	
	@Schema(description = "박스 코드")
	private String boxCode;
	
	@Schema(description = "박스 요금표 이미지")
	private MultipartFile boxFeeImage;
	
	@Schema(description = "박스 시간표 이미지")
	private MultipartFile boxTimeTableImage;
}
