package com.seocheon.fitian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seocheon.fitian.auth.annotation.AllowedRanks;
import com.seocheon.fitian.auth.annotation.CurrentUser;
import com.seocheon.fitian.auth.security.CustomUserDetails;
import com.seocheon.fitian.model.ApiResponse;
import com.seocheon.fitian.model.WodModel;
import com.seocheon.fitian.service.WodService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Box API", description="박스 관련 기능 API")
@RestController
@RequestMapping("/wod")
@SecurityRequirement(name="bearerAuth")
@CrossOrigin(origins = "http://localhost:8088")
public class WodController {

	@Autowired
	private WodService sv;
	
	//와드 정보 가져오기
	@Operation(summary = "와드 정보 가져오기", description = "boxCode와 wodDate로 와드 정보를 반환합니다.",
			security = {} )
	@GetMapping("/getWod")
    public ResponseEntity<ApiResponse<WodModel>> getWod(
    		@RequestBody WodModel model) {
    	WodModel wod = sv.getWod(model);
    	return ResponseEntity.ok(ApiResponse.success(wod));
    }
	
	//와드 생성
	@Operation(summary = "와드 생성", description = "owner 또는 manager가 와드를 등록합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PostMapping("/createWod")
    public ResponseEntity<ApiResponse<WodModel>> createWod(
    		@RequestBody WodModel model,
    		@CurrentUser CustomUserDetails userDetails) {
		
		if(!model.getBoxCode().equals(userDetails.getMember().getBoxCode())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("boxCode를 확인해주세요."));
		}
		
    	WodModel wod = sv.createWod(model);
    	return ResponseEntity.ok(ApiResponse.success(wod, wod.getWodDate()+" 와드가 등록되었습니다."));
    }
	
	//와드 수정
		@Operation(summary = "와드 수정", description = "owner 또는 manager가 와드를 수정합니다.",
				security = @SecurityRequirement(name = "bearerAuth"))
		@AllowedRanks({"owner","manager"})
	@PutMapping("/updateWod")
    public ResponseEntity<ApiResponse<WodModel>> updateWod(
    		@RequestBody WodModel model,
    		@CurrentUser CustomUserDetails userDetails) {
    	
		if(!model.getBoxCode().equals(userDetails.getMember().getBoxCode())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("소속 박스의 와드만 변경 가능합니다."));
		}
			
		WodModel wod = sv.updateWod(model);
    	
    	return ResponseEntity.ok(ApiResponse.success(wod, wod.getWodDate()+" 와드가 수정되었습니다."));
    }
	
	//와드 삭제
	@Operation(summary = "와드 삭제", description = "owner 또는 manager가 와드를 삭제합니다.",
		security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PutMapping("/deleteWod")
    public ResponseEntity<ApiResponse<Void>> deleteWod(
    		@RequestBody WodModel model,
    		@CurrentUser CustomUserDetails userDetails) {
    	sv.deleteWod(model);
    	return ResponseEntity.ok(ApiResponse.success(null, model.getWodDate()+" 와드가 삭제되었습니다."));
    }
	
}
