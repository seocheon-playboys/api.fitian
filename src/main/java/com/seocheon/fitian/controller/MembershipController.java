package com.seocheon.fitian.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seocheon.fitian.auth.annotation.AllowedRanks;
import com.seocheon.fitian.auth.annotation.CurrentUser;
import com.seocheon.fitian.auth.security.CustomUserDetails;
import com.seocheon.fitian.model.ApiResponse;
import com.seocheon.fitian.model.MembershipHistoryModel;
import com.seocheon.fitian.model.MembershipModel;
import com.seocheon.fitian.service.MembershipService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name="Membership API", description="회원권 관련 기능 API")
@RestController
@RequestMapping("/membership")
@SecurityRequirement(name="bearerAuth")
@RequiredArgsConstructor
public class MembershipController {

	private final MembershipService membershipService;
	
	//회원권 생성
	@Operation(summary = "회원권 생성하기", description = "owner,manager 유저가 회원권을 생성합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PostMapping("/createMembership")
	public ResponseEntity<ApiResponse<Void>> createMembership(
			@CurrentUser CustomUserDetails userDetails,
			@RequestBody MembershipModel request) {
		
		String boxCode = userDetails.getMember().getBoxCode();
		if(!boxCode.equals(request.getBoxCode())) return ResponseEntity.badRequest().body(ApiResponse.failure("boxCode를 확인해주세요"));
		
		membershipService.createMembership(request, userDetails.getMember().getNickname());
		
		return ResponseEntity.ok(ApiResponse.success(null,"회원권이 생성되었습니다."));
	}
	
	//회원권 가져오기
	@Operation(summary = "회원권 조회하기", description = "owner,manager 유저가 회원권을 조회합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@GetMapping("/getMembership")
	public ResponseEntity<ApiResponse<MembershipModel>> getMembership(
			@CurrentUser CustomUserDetails userDetails,
			@RequestParam("uid") String uid) {
		
		String boxCode = userDetails.getMember().getBoxCode();
		
		return ResponseEntity.ok(ApiResponse.success(membershipService.getMembership(boxCode, uid),"회원권을 조회했습니다."));
	}
	
	//회원권 갱신하기
	@Operation(summary = "회원권 갱신하기", description = "owner,manager 유저가 회원권을 갱신합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PostMapping("/updateMembership")
	public ResponseEntity<ApiResponse<Void>> updateMembership(
			@CurrentUser CustomUserDetails userDetails,
			@RequestBody MembershipModel request) {
		
		String boxCode = userDetails.getMember().getBoxCode();
		if(!boxCode.equals(request.getBoxCode())) return ResponseEntity.badRequest().body(ApiResponse.failure("boxCode를 확인해주세요"));
		
		membershipService.updateMembership(request, userDetails.getMember().getNickname());
		
		return ResponseEntity.ok(ApiResponse.success(null,"회원권이 갱신되었습니다."));
	}
	
	//회원권 삭제하기
	@Operation(summary = "회원권 삭제하기", description = "owner,manager 유저가 회원권을 삭제합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PostMapping("/deleteMembership")
	public ResponseEntity<ApiResponse<Void>> deleteMembership(
			@CurrentUser CustomUserDetails userDetails,
			@RequestBody MembershipModel request) {
		
		String boxCode = userDetails.getMember().getBoxCode();
		if(!boxCode.equals(request.getBoxCode())) return ResponseEntity.badRequest().body(ApiResponse.failure("boxCode를 확인해주세요"));
		
		membershipService.deleteMembership(request, userDetails.getMember().getNickname());
		
		return ResponseEntity.ok(ApiResponse.success(null,"회원권이 삭제되었습니다."));
	}
	
	//회원권 로그보기
	@Operation(summary = "회원권 로그보기", description = "owner,manager 유저가 회원권 로그를 조회합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@GetMapping("/viewMembershipHistory")
	public ResponseEntity<ApiResponse<List<MembershipHistoryModel>>> viewMembershipHistory(
			@CurrentUser CustomUserDetails userDetails,
			@RequestParam("membershipNo") int membershipNo) {
		
		return ResponseEntity.ok(ApiResponse.success(membershipService.viewMembershipHistory(membershipNo),"회원권 로그가 조회되었습니다."));
	}
}
