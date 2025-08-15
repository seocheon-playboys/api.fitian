package com.seocheon.fitian.dto.channel;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "채널 개설 요청 DTO")
public class ChannelCreateRequestDto {
	
	@Schema(description = "박스 코드")
	private String boxCode;
	
	@Schema(description = "채널 아이디")
	private String channelId;
	
	@Schema(description = "채널 이름")
	private String channelName;
	
	@Schema(description = "채널 타입")
	private String type;
	
	@Schema(description = "참여자 uid")
	private List<String> memberUids;
}
