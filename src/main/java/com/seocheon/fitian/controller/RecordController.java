package com.seocheon.fitian.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seocheon.fitian.auth.annotation.AllowedRanks;
import com.seocheon.fitian.auth.annotation.CurrentUser;
import com.seocheon.fitian.auth.security.CustomUserDetails;
import com.seocheon.fitian.dto.RecordResponseDto;
import com.seocheon.fitian.model.ApiResponse;
import com.seocheon.fitian.model.RecordModel;
import com.seocheon.fitian.model.WodModel;
import com.seocheon.fitian.service.RecordService;
import com.seocheon.fitian.service.WodService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name="Record API", description="기록 관련 기능 API")
@RestController
@RequestMapping("/record")
@SecurityRequirement(name="bearerAuth")
@CrossOrigin(origins = "http://localhost:8088")
@Slf4j
public class RecordController {

	@Autowired
	private RecordService recordService;
	
	@Autowired
	private WodService wodService;
	
	//레코드 가져오기
	@Operation(summary = "레코드 조회", description = "박스 구성원이 wodNo로 레코드를 가져옵니다",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"guest","member","owner","manager"})
	@GetMapping("/getRecord")
    public ResponseEntity<ApiResponse<List<RecordResponseDto>>> getRecord(
    		@Parameter(description="와드 넘버", required = true)
    		@RequestParam(value = "wodNo", required = true) int wodNo,
    		@CurrentUser CustomUserDetails userDetails) {
		
		WodModel wod = wodService.getWodByNo(wodNo);
		
		if(!wod.getBoxCode().equals(userDetails.getMember().getBoxCode())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("다른 박스의 레코드는 볼 수 없습니다."));
		}
		
		List<RecordResponseDto> recordList = recordService.getRecordList(wod);
    	
    	return ResponseEntity.ok(ApiResponse.success(recordList));
    }
	
	//레코드 생성
	@Operation(summary = "레코드 생성", description = "owner 또는 manager가 레코드를 생성합니다",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PostMapping("/createRecord")
    public ResponseEntity<ApiResponse<Void>> createRecord(
    		@RequestBody RecordModel model,
    		@CurrentUser CustomUserDetails userDetails) {
    	
		
		WodModel wod = wodService.getWodByNo(model.getWodNo());
		if(!wod.getBoxCode().equals(userDetails.getMember().getBoxCode())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("wodNo을 확인해주세요."));
		}
		
		if(model.getRecordModelList() == null || model.getRecordModelList().isEmpty()) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("레코드 목록이 비어있습니다."));
		}
		
		try {
			recordService.createRecord(model);
			return ResponseEntity.ok(ApiResponse.success(null, "레코드가 등록되었습니다.")) ;
		} catch (Exception ex) {
			log.error("레코드 생성 중 예기치 못한 오류 ", ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.failure("서버 오류로 레코드 생성에 실패했습니다."));
		}
		
    }
	
	//레코드 수정
	@Operation(summary = "레코드 수정", description = "owner 또는 manager가 레코드를 수정합니다",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PutMapping("/updateRecord")
    public ResponseEntity<ApiResponse<RecordResponseDto>> updateRecord(
    		@RequestBody RecordModel model,
    		@CurrentUser CustomUserDetails userDetails) {
    	RecordModel record = recordService.updateRecord(model);
    	return ResponseEntity.ok(ApiResponse.success(RecordResponseDto.from(record), "레코드가 수정되었습니다."));
    }
	
	//레코드 삭제
	@Operation(summary = "레코드 삭제", description = "owner 또는 manager가 해당 레코드를 삭제합니다",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PutMapping("/deleteRecord")
    public ResponseEntity<ApiResponse<Void>> deleteRecord(
    		@Parameter(description="레코드 넘버", required = true)
    		@RequestParam(value = "recordNo", required = true) int recordNo,
    		@CurrentUser CustomUserDetails userDetails) {
    	
		RecordModel record = recordService.getRecord(recordNo);
		WodModel wod = wodService.getWodByNo(recordService.getRecord(recordNo).getWodNo());
		
		if(!wod.getBoxCode().equals(userDetails.getMember().getBoxCode())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("다른 박스의 레코드는 삭제할 수 없습니다."));
		}
		
    	try {
    		recordService.deleteRecord(recordNo);
			return ResponseEntity.ok(ApiResponse.success(null,record.getName()+"님의 "+wod.getWodDate()+" 레코드가 삭제 되었습니다.")) ;
		} catch (Exception ex) {
			log.error("레코드 삭제 중 예기치 못한 오류 ", ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.failure("서버 오류로 레코드 삭제에 실패했습니다."));
		}

    }
	
	//레코드 전체 삭제
	@Operation(summary = "레코드 전체 삭제", description = "owner 또는 manager가 해당 와드의 레코드를 전체 삭제합니다",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PutMapping("/deleteAllRecord")
    public ResponseEntity<ApiResponse<Void>> deleteAllRecord(
    		@Parameter(description="와드 넘버", required = true)
    		@RequestParam(value = "wodNo", required = true) int wodNo,
    		@CurrentUser CustomUserDetails userDetails) {
		
		WodModel wod = wodService.getWodByNo(wodNo);
		
		if(!wod.getBoxCode().equals(userDetails.getMember().getBoxCode())) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("다른 박스의 레코드는 삭제할 수 없습니다."));
		}
    	
    	try {
    		recordService.deleteAllRecord(wodNo);
			return ResponseEntity.ok(ApiResponse.success(null, wod.getWodDate()+" 레코드가 전체 삭제 되었습니다.")) ;
		} catch (Exception ex) {
			log.error("레코드 삭제 중 예기치 못한 오류 ", ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.failure("서버 오류로 레코드 전체 삭제에 실패했습니다."));
		}
    	
    }
	
}
