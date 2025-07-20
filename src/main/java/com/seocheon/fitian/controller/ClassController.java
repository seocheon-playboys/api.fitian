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
	@PostMapping("/class")
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
		
		return ResponseEntity.ok(ApiResponse.success(classService.getClasses(dto),"수업이 작성되었습니다."));
	}
	
}
