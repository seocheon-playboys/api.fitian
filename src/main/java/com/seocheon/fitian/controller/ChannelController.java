package com.seocheon.fitian.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seocheon.fitian.auth.annotation.AllowedRanks;
import com.seocheon.fitian.auth.annotation.CurrentUser;
import com.seocheon.fitian.auth.security.CustomUserDetails;
import com.seocheon.fitian.dto.channel.ChannelCreateRequestDto;
import com.seocheon.fitian.dto.channel.ChannelInviteRequestDto;
import com.seocheon.fitian.dto.channel.ChannelListResponseDto;
import com.seocheon.fitian.dto.channel.ChannelParticipantResponseDto;
import com.seocheon.fitian.dto.channel.ChannelResponseDto;
import com.seocheon.fitian.model.ApiResponse;
import com.seocheon.fitian.service.ChannelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name="Channel API", description="채널 관련 기능 API")
@RestController
@RequestMapping("/channel")
@SecurityRequirement(name="bearerAuth")
@CrossOrigin(origins = "http://localhost:8088")
@RequiredArgsConstructor
public class ChannelController {

	private final ChannelService channelService;
	
	@Operation(summary = "채팅방 생성", description = "채팅방(채널)을 생성합니다. 매니저/오너만 생성할 수 있습니다.")
	@AllowedRanks({"owner", "manager"})
	@PostMapping("/create")
	public ResponseEntity<ApiResponse<ChannelResponseDto>> createChannel(
			@RequestBody ChannelCreateRequestDto request,
			@CurrentUser CustomUserDetails userDetails) {
		
		ChannelResponseDto response = channelService.createChannel(request, userDetails.getMember());
		return ResponseEntity.ok(ApiResponse.success(response, "채널 개설 완료"));
	}
	
	@Operation(summary = "채널 목록 조회", description = "로그인한 사용자의 boxCode에 속한 채널 리스트를 반환합니다.")
	@AllowedRanks({"owner", "manager", "member"})
	@GetMapping("/getChannels")
	public ResponseEntity<ApiResponse<List<ChannelListResponseDto>>> getChannels(
			@CurrentUser CustomUserDetails userDetails) {
		
		String boxCode = userDetails.getMember().getBoxCode();
		List<ChannelListResponseDto> result = channelService.getChannelList(boxCode);
		
		return ResponseEntity.ok(ApiResponse.success(result));
	}
	
	@Operation(summary = "채널 참여자 목록 조회", description = "채널에 참여 중인 모든 사용자의 정보를 반환합니다.")
	@AllowedRanks({"owner", "manager", "member"})
	@GetMapping("/{channelId}/participants")
	public ResponseEntity<ApiResponse<List<ChannelParticipantResponseDto>>> getParticipants(
			@PathVariable String channelId,
			@CurrentUser CustomUserDetails userDetails) {
		
		String boxCode = userDetails.getMember().getBoxCode();
		List<ChannelParticipantResponseDto> result = channelService.getChannelParticipants(boxCode, channelId);
		
		return ResponseEntity.ok(ApiResponse.success(result));
	}
	
	@Operation(summary = "채널 참여자 초대", description = "기존 채널에 새로운 참여자를 초대합니다.")
	@AllowedRanks({"owner", "manager"})
	@PostMapping("/{channelId}/participants")
	public ResponseEntity<ApiResponse<Void>> inviteParticipants(
			@PathVariable String channelId,
			@RequestBody ChannelInviteRequestDto request,
			@CurrentUser CustomUserDetails userDetails) {
		String boxCode = userDetails.getMember().getBoxCode();
		String inviterUid = userDetails.getUsername();
		
		channelService.inviteParticipants(boxCode, channelId, request.getMemberUids(), inviterUid);
		
		return ResponseEntity.ok(ApiResponse.success(null, "채널에 참여자가 초대되었습니다."));
	}
	
	@Operation(summary = "채널 참여자 제거", description = "자기 자신은 퇴장, 관리자(owner/manager)는 다른 사람을 제거할 수 있습니다.")
	@AllowedRanks({"owner", "manager", "member"})
	@DeleteMapping("/{channelId}/participants/{targetUid}")
	public ResponseEntity<ApiResponse<Void>> deleteParticipant(
			@PathVariable String channelId,
			@PathVariable String targetUid,
			@CurrentUser CustomUserDetails userDetails) {
		
		boolean isSelf = userDetails.getUsername().equals(targetUid);
		boolean isManager = userDetails.getMember().getRank().equals("owner") || userDetails.getMember().getRank().equals("manager");
		
		if(!isSelf && !isManager) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("본인 또는 관리자만 제거 할 수 있습니다."));
		}
		
		channelService.deleteParticipant(userDetails.getMember().getBoxCode(), channelId, targetUid);
		
		String message = isSelf ? "채널에서 퇴장했습니다." : "참여자를 채널에서 제거했습니다.";
		
		return ResponseEntity.ok(ApiResponse.success(null, message));
	}
	
	@Operation(summary = "채널 입장 유효성 확인", description = "로그인한 사용자가 해당 채널에 참여 중인지 확인합니다.")
	@AllowedRanks({"owner", "manager", "member"})
	@GetMapping("/{channelId}/enter")
	public ResponseEntity<ApiResponse<Void>> canEnter(
			@PathVariable String channelId,
			@CurrentUser CustomUserDetails userDetails) {
		
		String boxCode = userDetails.getMember().getBoxCode();
		boolean canEnter = channelService.canEnterChannel(boxCode, channelId, userDetails.getUsername());
		
		if(!canEnter) {
			return ResponseEntity.status(403).body(ApiResponse.failure("채널에 참여중이 아닙니다."));
		}
		
		return ResponseEntity.ok(ApiResponse.success(null,"입장 가능합니다."));
	}
	
	@Operation(summary = "채널 삭제", description = "오너 또는 매니저가 채널을 삭제합니다.")
	@AllowedRanks({"owner", "manager", "member"})
	@DeleteMapping("/{channelId}/delete")
	public ResponseEntity<ApiResponse<Void>> deleteChannel(
			@PathVariable String channelId,
			@CurrentUser CustomUserDetails userDetails) {
		
		String boxCode = userDetails.getMember().getBoxCode();
		
		channelService.deleteChannel(boxCode, channelId);
		
		return ResponseEntity.ok(ApiResponse.success(null,"채널이 삭제되었습니다."));
	}
}
