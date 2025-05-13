package com.seocheon.fitian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seocheon.fitian.auth.annotation.AllowedRanks;
import com.seocheon.fitian.auth.annotation.CurrentUser;
import com.seocheon.fitian.auth.security.CustomUserDetails;
import com.seocheon.fitian.model.ApiResponse;
import com.seocheon.fitian.model.FCMtokenModel;
import com.seocheon.fitian.service.FCMtokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name="FCMtoken API", description="FCM 관련 기능 API")
@RestController
@RequestMapping("/token")
@SecurityRequirement(name="bearerAuth")
@CrossOrigin(origins = "http://localhost:8088")
@Slf4j
public class FCMtokenController {

	@Autowired
	private FCMtokenService sv;
	
	
	//FCM 토큰 가져오기
	@Operation(summary = "FCM 토큰 가져오기", description = "해당 유저의 FCM 토큰을 조회합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"guest","member","owner","manager"})
	@GetMapping("/getToken")
    public ResponseEntity<ApiResponse<FCMtokenModel>> getToken(
    		@RequestBody FCMtokenModel model,
    		@CurrentUser CustomUserDetails userDetails) {

		if(!model.getUid().equals(userDetails.getUsername())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("uid를 확인해주세요."));
		}
		
    	return ResponseEntity.ok(ApiResponse.success(sv.getToken(model)));
    }
	
	//FCM 토큰 생성
	@Operation(summary = "FCM 토큰 생성", description = "해당 유저의 FCM 토큰을 생성합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"guest","member","owner","manager"})
	@PostMapping("/createToken")
    public ResponseEntity<ApiResponse<Void>> createToken(
    		@RequestBody FCMtokenModel model,
    		@CurrentUser CustomUserDetails userDetails) {
		
		if(!model.getUid().equals(userDetails.getUsername())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("uid를 확인해주세요."));
		}
    	
    	try {
    		sv.createToken(model);
			return ResponseEntity.ok(ApiResponse.success(null, "토큰이 생성되었습니다.")) ;
		} catch (Exception ex) {
			log.error("토큰 생성 중 예기치 못한 오류 ", ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.failure("서버 오류로 토큰 생성에 실패했습니다."));
		}
    }
}
