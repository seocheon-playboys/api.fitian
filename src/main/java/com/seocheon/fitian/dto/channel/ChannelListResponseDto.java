package com.seocheon.fitian.dto.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "채널 목록 반환 DTO")
public class ChannelListResponseDto {
	
	@Schema(description = "채널 아이디")
	private String channelId;
	
	@Schema(description = "채널 이름")
	private String channelName;
	
	@Schema(description = "참여자 uid")
	private String type;
}
