package com.seocheon.fitian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seocheon.fitian.auth.annotation.AllowedRanks;
import com.seocheon.fitian.auth.annotation.CurrentUser;
import com.seocheon.fitian.auth.security.CustomUserDetails;
import com.seocheon.fitian.dto.MemberResponseDto;
import com.seocheon.fitian.dto.MembersResponseDto;
import com.seocheon.fitian.model.ApiResponse;
import com.seocheon.fitian.model.MemberModel;
import com.seocheon.fitian.service.MemberService;

@RestController
@RequestMapping("/member")
@CrossOrigin(origins = "http://localhost:8088")
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	//본인 정보 가져오기
	@GetMapping("/me")
    public String getMyInfo(
    		@CurrentUser CustomUserDetails userDetails) {
		
    	return "현재 로그인한 uid: "+userDetails.getMember().getUid();
    }
	
	//본인 정보 바꾸기
	@PutMapping("/me")
	@AllowedRanks({"guest","member","owner","manager"})
	public ResponseEntity<ApiResponse<MemberResponseDto>> updateMyInfo(
			@RequestBody MemberModel member,
			@CurrentUser CustomUserDetails userDetails) {
		
		if(member.getUid() == null || member.getUid().trim().isEmpty()) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("uid가 누락되었습니다."));
		}
		
		if(!member.getUid().equals(userDetails.getMember().getUid())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("본인만 수정할 수 있습니다."));
		}
		
		MemberResponseDto dto =  memberService.updateMyInfo(member, userDetails);
	    return ResponseEntity.ok(ApiResponse.success(dto, "내 정보가 수정되었습니다."));
	}
	
	//관리자의 다른 회원 정보 수정
	@AllowedRanks({"owner","manager"})
	@PutMapping("/admin/updateMember")
    public ResponseEntity<ApiResponse<MemberResponseDto>> updateMember(
    		@RequestBody MemberModel member,
    		@CurrentUser CustomUserDetails userDetails) {
		
		if(member.getRank().equals("owner")) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("바꿀 수 없는 회원입니다."));
		}
		
		if(!member.getBoxCode().equals(userDetails.getMember().getBoxCode())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("다른 박스의 회원 정보는 변경할 수 없습니다."));
		}
		
		MemberResponseDto dto = memberService.updateMember(member);
		
    	return ResponseEntity.ok(ApiResponse.success(dto, "회원 정보가 수정되었습니다."));
    }
	
	//owner 양도
	@AllowedRanks({"owner"})
	@PutMapping("/admin/changeOwner")
    public ResponseEntity<ApiResponse<MemberResponseDto>> changeOwner(
    		@RequestBody MemberModel member,
    		@CurrentUser CustomUserDetails userDetails) {
		
		if(!member.getRank().equals("manager")) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("매니저에게만 양도 가능합니다."));
		}
		
		MemberResponseDto dto = memberService.changeOwner(member, userDetails);
		
    	return ResponseEntity.ok(ApiResponse.success(dto, "내 정보가 수정되었습니다."));
    }
	
	@AllowedRanks({"member","owner","manager"})
	@PutMapping("/getMember")
    public ResponseEntity<ApiResponse<MemberResponseDto>> getMember(
    		@RequestBody MemberModel member,
    		@CurrentUser CustomUserDetails userDetails) {
    	MemberModel m = memberService.findByUid(member.getUid());
    	
    	if(!member.getBoxCode().equals(userDetails.getMember().getBoxCode())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("다른 박스의 회원 정보는 열람할 수 없습니다."));
		}
		
    	return ResponseEntity.ok(ApiResponse.success(MemberResponseDto.from(m)));
    }
	
	@AllowedRanks({"owner","manager"})
	@PutMapping("/getAllMember")
	public ResponseEntity<ApiResponse<MembersResponseDto>> getAllMember(
    		@RequestBody MemberModel member,
    		@CurrentUser CustomUserDetails userDetails) {
    	
    	if(!member.getBoxCode().equals(userDetails.getMember().getBoxCode())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("다른 박스의 회원 정보는 열람할 수 없습니다."));
		}
    	
    	MembersResponseDto dto = memberService.getAllMember(member);
		
    	return ResponseEntity.ok(ApiResponse.success(dto));
    }
	
	@PutMapping("/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivate(
    		@CurrentUser CustomUserDetails userDetails) {
		
		memberService.deactivate(userDetails.getUsername());
    	return ResponseEntity.ok(ApiResponse.success(null, "정상적으로 탈퇴되었습니다.")) ;
    }
	
}
