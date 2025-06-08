package com.seocheon.fitian.dto.channel;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "채널 참여자 초대 DTO")
public class ChannelInviteRequestDto {
	
	@Schema(description = "참여자 uid")
	private List<String> memberUids;
}
