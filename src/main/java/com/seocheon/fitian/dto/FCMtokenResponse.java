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
@Schema(description = "FCM 토큰 조회 응답 DTO")
public class FCMtokenResponse {
	
	@Schema(description = "앱 인스턴스 고유 식별자")
	private String device_id;
	
	@Schema(description = "firebase에서 발급된 토큰")
	private String token;

	@Schema(description = "토큰 활성 여부")
	private Boolean is_active;
	
	@Schema(description = "토큰 최종 업데이트 시간")
	private String update_at;
}
