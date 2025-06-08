package com.seocheon.fitian.dto.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "채널 참여자 목록 반환 DTO")
public class ChannelParticipantResponseDto {
	
	@Schema(description = "멤버 uid")
	private String uid;
	
	@Schema(description = "멤버 이름")
	private String name;
	
	@Schema(description = "맴버 등급")
	private String rank;

}
