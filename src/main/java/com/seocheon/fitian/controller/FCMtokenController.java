package com.seocheon.fitian.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.seocheon.fitian.auth.annotation.AllowedRanks;
import com.seocheon.fitian.auth.annotation.CurrentUser;
import com.seocheon.fitian.auth.security.CustomUserDetails;
import com.seocheon.fitian.dto.FCMtokenCreateRequest;
import com.seocheon.fitian.dto.FCMtokenResponse;
import com.seocheon.fitian.mapper.FCMtokenMapper;
import com.seocheon.fitian.model.ApiResponse;
import com.seocheon.fitian.model.FCMtokenModel;
import com.seocheon.fitian.service.FCMtokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
	private FCMtokenMapper mapper;
	
	
	//FCM 토큰 가져오기
	@Operation(summary = "FCM 토큰 가져오기", description = "해당 유저의 FCM 토큰을 조회합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"guest","member","owner","manager"})
	@PostMapping("/getToken")
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
	
	
	//FCM 토큰 생성
	@Operation(summary = "FCM 토큰 생성 및 업데이트", description = "해당 유저의 FCM 토큰을 생성 또는 업데이트합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"guest","member","owner","manager"})
	@PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
    		@Valid @RequestBody FCMtokenCreateRequest request,
	   		@CurrentUser CustomUserDetails userDetails) {
    	
    	try {
    		sv.upsertToken(userDetails.getUsername(), request);
			return ResponseEntity.ok(ApiResponse.success(null, "토큰이 생성되었습니다.")) ;
		} catch (Exception ex) {
			log.error("토큰 생성 중 예기치 못한 오류 ", ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.failure("서버 오류로 토큰 생성에 실패했습니다."));
		}
    }

	//FCM 토큰 가져오기
	@Operation(summary = "FCM 토큰 가져오기", description = "해당 유저의 FCM 토큰을 조회합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"guest","member","owner","manager"})
	@GetMapping("/getTokenList")
    public ResponseEntity<ApiResponse<List<FCMtokenResponse>>> getTokenList(
    		@CurrentUser CustomUserDetails userDetails) {
		
		List<FCMtokenResponse> response = mapper.findByUid(userDetails.getUsername()).stream()
				.map(model -> new FCMtokenResponse(
						model.getDevice_id(),
						model.getToken(),
						model.getIs_active(),
						model.getUpdate_at()
				)).collect(Collectors.toList());
		
    	return ResponseEntity.ok(ApiResponse.success(response));
	}
	
	//특정 디바이스 토큰 조회
	@Operation(summary = "특정 디바이스 토큰 조회", description = "현재 사용자의 디바이스 ID에 해당하는 토큰을 가져옵니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"guest","member","owner","manager"})
	@GetMapping("/{deviceID}")
    public ResponseEntity<ApiResponse<FCMtokenResponse>> getByDevice(
    		@PathVariable String device_id,
    		@CurrentUser CustomUserDetails userDetails) {
		
		FCMtokenModel model = mapper.findByUid(userDetails.getUsername()).stream()
				.filter(m -> m.getDevice_id().equals(device_id))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND, "Device ID not found: "+device_id));
		
		FCMtokenResponse resp = new FCMtokenResponse(
				model.getDevice_id(),
				model.getToken(),
				model.getIs_active(),
				model.getUpdate_at());
				
    	return ResponseEntity.ok(ApiResponse.success(resp));
    }
	
	//토큰 일괄 삭제
	@Operation(summary = "FCM 토큰 삭제", description = "현재 사용자의 특정 토큰을 삭제합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"guest","member","owner","manager"})
	@DeleteMapping("/{token}")
    public ResponseEntity<ApiResponse<Void>> deleteToken(
    		@PathVariable String token,
    		@CurrentUser CustomUserDetails userDetails) {
		
		mapper.findByUid(userDetails.getUsername()).stream()
				.filter(m -> m.getToken().equals(token))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND, "Token not found for current user"));
		
		sv.deactivateToken(token);
				
    	return ResponseEntity.ok(ApiResponse.success(null, "해당 토큰이 삭제되었습니다."));
    }
}
