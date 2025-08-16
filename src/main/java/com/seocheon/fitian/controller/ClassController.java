package com.seocheon.fitian.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seocheon.fitian.auth.annotation.AllowedRanks;
import com.seocheon.fitian.auth.annotation.CurrentUser;
import com.seocheon.fitian.auth.security.CustomUserDetails;
import com.seocheon.fitian.dto.classes.SearchClassDTO;
import com.seocheon.fitian.model.ApiResponse;
import com.seocheon.fitian.model.ClassModel;
import com.seocheon.fitian.model.ClassParticipantModel;
import com.seocheon.fitian.service.ClassService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name="Class API", description="수업 관련 기능 API")
@RestController
@RequestMapping("/class")
@SecurityRequirement(name="bearerAuth")
@RequiredArgsConstructor
public class ClassController {
	
	
	private final ClassService classService;

	//수업 작성
	@Operation(summary = "수업 작성하기", description = "owner,manager 유저가 수업을 생성합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PostMapping("/create")
	public ResponseEntity<ApiResponse<Void>> createClass(
			@CurrentUser CustomUserDetails userDetails,
			@RequestBody List<ClassModel> requestList) {
		
		String boxCode = userDetails.getMember().getBoxCode();
		classService.createClass(boxCode, requestList);
		
		return ResponseEntity.ok(ApiResponse.success(null,"수업이 작성되었습니다."));
	}
	
	//수업 가져오기
	@Operation(summary = "수업 가져오기", description = "해당박스의 회원이 지정한 날짜의 수업을 가져옵니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager","member"})
	@PostMapping("/getClasses")
	public ResponseEntity<ApiResponse<List<ClassModel>>> getClasses(
			@CurrentUser CustomUserDetails userDetails,
			@RequestBody SearchClassDTO dto) {
		
		String boxCode = userDetails.getMember().getBoxCode();
		dto.setBoxCode(boxCode);
		
		return ResponseEntity.ok(ApiResponse.success(classService.getClasses(dto, userDetails.getUsername()),"수업이 작성되었습니다."));
	}
	
	//수업 참여하기
	@Operation(summary = "수업 참여하기", description = "해당박스의 회원이 지정한 날짜의 수업에 참여합니다",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager","member"})
	@PostMapping("/joinClass")
	public ResponseEntity<ApiResponse<Void>> joinClass(
			@CurrentUser CustomUserDetails userDetails,
			@RequestBody ClassParticipantModel model) {
		
		String boxCode = userDetails.getMember().getBoxCode();
		classService.joinClass(boxCode, model);
		return ResponseEntity.ok(ApiResponse.success(null,"수업에 참여되었습니다."));
	}
	
	//수업 취소하기
	@Operation(summary = "수업 취소하기", description = "해당박스의 회원이 지정한 날짜의 참여신청 수업을 취소합니다",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager","member"})
	@PostMapping("/cancelClass")
	public ResponseEntity<ApiResponse<Void>> cancelClass(
			@CurrentUser CustomUserDetails userDetails,
			@RequestBody ClassParticipantModel model) {
		
		String boxCode = userDetails.getMember().getBoxCode();
		classService.cancelClass(boxCode, model);
		return ResponseEntity.ok(ApiResponse.success(null,"수업이 취소되었습니다."));
	}
	
	//수업 참여자 반환
	@Operation(summary = "수업 참여자 반환", description = "해당수업의 참여자 리스트를 반환합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PostMapping("/getClassParticipant")
	public ResponseEntity<ApiResponse<List<ClassParticipantModel>>> getMembersByClassNo(
			@CurrentUser CustomUserDetails userDetails,
			@RequestBody ClassModel model) {
		int classNo = model.getClassNo();
		return ResponseEntity.ok(ApiResponse.success(classService.findMembersByClassNo(classNo),"수업참여자 정보를 반환합니다."));
	}
	
	//수업 수정하기
	@Operation(summary = "수업 수정하기", description = "owner,manager 유저가 수업을 수정합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PostMapping("/update")
	public ResponseEntity<ApiResponse<Void>> updateClass(
			@CurrentUser CustomUserDetails userDetails,
			@RequestBody ClassModel model) {
		
		String boxCode = userDetails.getMember().getBoxCode();
		classService.updateClass(boxCode, model);
		
		return ResponseEntity.ok(ApiResponse.success(null,"수업이 수정되었습니다."));
	}
	
	//수업 삭제하기
	@Operation(summary = "수업 삭제하기", description = "owner,manager 유저가 수업을 삭제합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PostMapping("/delete")
	public ResponseEntity<ApiResponse<Void>> deleteClass(
			@CurrentUser CustomUserDetails userDetails,
			@RequestBody ClassModel model) {
		
		String boxCode = userDetails.getMember().getBoxCode();
		classService.deleteClass(boxCode, model);
		
		return ResponseEntity.ok(ApiResponse.success(null,"수업이 삭제되었습니다."));
	}
}
