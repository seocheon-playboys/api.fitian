package com.seocheon.fitian.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "FCM 토큰 등록 요청 DTO")
public class FCMtokenCreateRequest {
	
	@Schema(description = "앱 인스턴스 고유 식별자")
	@NotBlank(message = "device_id는 필수입니다.")
	private String device_id;
	
	@Schema(description = "firebase 발급 토큰")
	@NotBlank(message = "firebase 토큰은 필수입니다.")
	private String token;
}
