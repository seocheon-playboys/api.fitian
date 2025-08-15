package com.seocheon.fitian.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.seocheon.fitian.auth.annotation.AllowedRanks;
import com.seocheon.fitian.auth.annotation.CurrentUser;
import com.seocheon.fitian.auth.security.CustomUserDetails;
import com.seocheon.fitian.dto.BoxResponseDto;
import com.seocheon.fitian.dto.BoxSummaryDto;
import com.seocheon.fitian.dto.UpdateBoxRequest;
import com.seocheon.fitian.dto.channel.ChannelCreateRequestDto;
import com.seocheon.fitian.model.ApiResponse;
import com.seocheon.fitian.model.BoxModel;
import com.seocheon.fitian.service.BoxService;
import com.seocheon.fitian.service.ChannelService;
import com.seocheon.fitian.service.S3Service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name="Box API", description="박스 관련 기능 API")
@RestController
@RequestMapping("/box")
@SecurityRequirement(name="bearerAuth")
@CrossOrigin(origins = "http://localhost:8088")
public class BoxController {

	@Autowired
	private BoxService boxService;
	
	@Autowired
	private S3Service s3Service;
	
	//박스 정보 가져오기
	@Operation(summary = "박스 정보 가져오기", description = "boxCode 또는 boxName으로 박스 정보를 반환합니다.",
			security = {} )
	@GetMapping("/getBox")
    public ResponseEntity<ApiResponse<BoxResponseDto>> getBox(
    		@Parameter(description="해당 박스 코드", required = true)
    		@RequestParam(value="boxCode") String boxCode) {
		
		BoxModel box = boxService.getBoxByCode(boxCode);
    	return ResponseEntity.ok(ApiResponse.success(BoxResponseDto.from(box)));
    }
	
	//박스 리스트 가져오기
	@Operation(summary = "박스 요약 목록 가져오기", description = "전체 박스의 코드,이름,주소를 반환합니다.",
			security = {} )
	@GetMapping("/getBoxList")
    public ResponseEntity<ApiResponse<List<BoxSummaryDto>>> getBoxList() {
		//일정 갯수 추가 필요
		List<BoxSummaryDto> summaries = boxService.getBoxList();
    	return ResponseEntity.ok(ApiResponse.success(summaries));
    }
	
	//박스 생성
	@Operation(summary = "박스 생성", description = "가입한 회원이 새로운 박스를 생성합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"guest"})
	@PostMapping("/createBox")
    public ResponseEntity<ApiResponse<BoxResponseDto>> createBox(
    		@RequestBody BoxModel model,
    		@CurrentUser CustomUserDetails userDetails) {
		
		BoxModel box = boxService.createBox(model, userDetails);
    	
    	return ResponseEntity.ok(ApiResponse.success(BoxResponseDto.from(box), box.getBoxName()+" 박스가 생성되었습니다."));
    }
	
	//박스 정보 수정
	@Operation(summary = "박스 정보 수정", description = "owner 또는 manager가 박스 정보를 수정합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PutMapping("/updateBox")
    public ResponseEntity<ApiResponse<BoxResponseDto>> updateBox(
    		@RequestBody UpdateBoxRequest request,
    		@CurrentUser CustomUserDetails userDetails) {
    	BoxModel box = boxService.updateBox(request, userDetails.getMember().getBoxCode());
    	return ResponseEntity.ok(ApiResponse.success(BoxResponseDto.from(box), " 박스 정보가 수정되었습니다."));
    }
	
	@Operation(summary = "박스 정보 이미지 등록", description = "owner 또는 manager가 박스 정보 이미지를 등록합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PostMapping(value = "/addBoxInfoImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BoxResponseDto>> addBoxInfoImage(
    		@RequestPart(value = "boxCode") String boxCode,
            @RequestPart(value = "boxFeeImage", required = false) MultipartFile boxFeeImage,
            @RequestPart(value = "boxTimeTableImage", required = false) MultipartFile boxTimeTableImage,
    		@CurrentUser CustomUserDetails userDetails) {

		UpdateBoxRequest boxReq = new UpdateBoxRequest();
		
    	try {
    		if(boxFeeImage != null && !boxFeeImage.isEmpty()) {
    			boxReq.setBoxFeeUrl(s3Service.uploadFile(boxFeeImage));
    		} 
    		if(boxTimeTableImage != null && !boxTimeTableImage.isEmpty()) {
    			boxReq.setBoxTimeTableUrl(s3Service.uploadFile(boxTimeTableImage));
    		} 
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("이미지 업로드에 실패했습니다."));
		}
    	
    	BoxModel box = boxService.updateBox(boxReq, userDetails.getMember().getBoxCode());
    	
    	return ResponseEntity.ok(ApiResponse.success(BoxResponseDto.from(box), " 박스 정보 이미지가 등록되었습니다."));
	}
	
//	@PutMapping(value = "/exaddBoxInfoImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseModel exaddBoxInfoImage(
//    		@RequestPart(value = "boxFeeImage", required = false) MultipartFile boxFeeImage,
//    		@RequestPart(value = "boxTimeTableImage", required = false) MultipartFile boxTimeTableImage,
//    		@RequestPart(value = "BoxModel") BoxModel model) {
//    	ResponseModel res = new ResponseModel();
//
//    	try {
//    		if(boxFeeImage != null && !boxFeeImage.isEmpty()) {
//    			model.setBoxFeeUrl(s3Service.uploadFile(boxFeeImage));
//    		} 
//    		if(boxTimeTableImage != null && !boxTimeTableImage.isEmpty()) {
//    			model.setBoxTimeTableUrl(s3Service.uploadFile(boxTimeTableImage));
//    		} 
//		} catch (Exception e) {
//			res.setMessage("File upload failed: " + e.getMessage());
//			return res;
//		}
//    	
//    	res = boxService.updateBox(model);
//    	res.setBoxModel(model);
//		return res;
//    }
	
	@Operation(summary = "박스 정보 이미지 수정", description = "owner 또는 manager가 박스 정보 이미지를 수정합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PutMapping(value = "/updateBoxInfoImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BoxResponseDto>> updateBoxInfoImage(
    		@RequestPart(value = "boxCode") String boxCode,
            @RequestPart(value = "boxFeeImage", required = false) MultipartFile boxFeeImage,
            @RequestPart(value = "boxTimeTableImage", required = false) MultipartFile boxTimeTableImage,
    		@CurrentUser CustomUserDetails userDetails) {

		BoxModel box = boxService.getBoxByCode(userDetails.getMember().getBoxCode());
		UpdateBoxRequest boxReq = new UpdateBoxRequest();
		
    	try {
    		if(boxFeeImage != null && !boxFeeImage.isEmpty()) {
    			s3Service.deleteFile(box.getBoxFeeUrl());
    			boxReq.setBoxFeeUrl(s3Service.uploadFile(boxFeeImage));
    		} 
    		if(boxTimeTableImage != null && !boxTimeTableImage.isEmpty()) {
    			s3Service.deleteFile(box.getBoxTimeTableUrl());
    			boxReq.setBoxTimeTableUrl(s3Service.uploadFile(boxTimeTableImage));
    		} 
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("이미지 업로드에 실패했습니다."));
		}
    	
    	BoxModel updatedBox = boxService.updateBox(boxReq, userDetails.getMember().getBoxCode());
    	return ResponseEntity.ok(ApiResponse.success(BoxResponseDto.from(updatedBox), " 박스 정보 이미지가 수정되었습니다."));
    }
	
//	@PostMapping(value = "exupdateBoxInfoImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseModel exupdateBoxInfoImage(
//    		@RequestPart(value = "boxFeeImage", required = false) MultipartFile boxFeeImage,
//    		@RequestPart(value = "boxTimeTableImage", required = false) MultipartFile boxTimeTableImage,
//    		@RequestPart(value = "BoxModel") BoxModel model) {
//    	ResponseModel res = new ResponseModel();
//
//    	try {
//    		if(boxFeeImage != null && !boxFeeImage.isEmpty()) {
//    			s3Service.deleteFile(model.getBoxFeeUrl());
//    			model.setBoxFeeUrl(s3Service.uploadFile(boxFeeImage));
//    		} 
//    		if(boxTimeTableImage != null && !boxTimeTableImage.isEmpty()) {
//    			s3Service.deleteFile(model.getBoxTimeTableUrl());
//    			model.setBoxTimeTableUrl(s3Service.uploadFile(boxTimeTableImage));
//    		} 
//		} catch (Exception e) {
//			res.setMessage("File upload failed: " + e.getMessage());
//			return res;
//		}
//    	
//    	res = boxService.updateBox(model);
//    	res.setBoxModel(model);
//		return res;
//    }
	
	@Operation(summary = "박스 정보 이미지 삭제", description = "owner 또는 manager가 박스 정보 이미지를 삭제합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@AllowedRanks({"owner","manager"})
	@PutMapping(value = "/deleteBoxInfoImage")
    public ResponseEntity<ApiResponse<BoxResponseDto>> deleteBoxInfoImage(
    		@Parameter(description="요금표 이미지 url", required = false)
    		@RequestParam(value = "boxFeeImageUrl", required = false) String boxFeeImageUrl,
    		
    		@Parameter(description="시간표 이미지 url", required = false)
    		@RequestParam(value = "boxTimeTableUrl", required = false) String boxTimeTableUrl,
    		@CurrentUser CustomUserDetails userDetails) {

		UpdateBoxRequest boxReq = new UpdateBoxRequest();
		BoxModel box = boxService.getBoxByCode(userDetails.getMember().getBoxCode());
		
    	try {
    		if(boxFeeImageUrl != null && !boxFeeImageUrl.equals("")) {
    			
    			if(box.getBoxFeeUrl() != boxFeeImageUrl) {
    				return ResponseEntity.badRequest().body(ApiResponse.failure("이미지 url이 일치하지 않습니다."));
    			}
    			
    			s3Service.deleteFile(boxFeeImageUrl);
    			boxReq.setBoxFeeUrl("NONE");
    		} 
    		if(boxTimeTableUrl != null && !boxTimeTableUrl.equals("")) {
    			
    			if(box.getBoxTimeTableUrl() != boxTimeTableUrl) {
    				return ResponseEntity.badRequest().body(ApiResponse.failure("이미지 url이 일치하지 않습니다."));
    			}
    			
    			s3Service.deleteFile(boxTimeTableUrl);
    			boxReq.setBoxTimeTableUrl("NONE");
    		}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("이미지 삭제에 실패했습니다."));
		}
    	
    	BoxModel updatedBox = boxService.updateBox(boxReq, userDetails.getMember().getBoxCode());
    	
    	return ResponseEntity.ok(ApiResponse.success(BoxResponseDto.from(updatedBox), " 박스 정보 이미지가 삭제되었습니다."));
    }
	
//	@PutMapping(value = "/exdeleteBoxInfoImage")
//    public ResponseModel exdeleteBoxInfoImage(@RequestBody BoxModel model) {
//    	ResponseModel res = new ResponseModel();
//
//    	try {
//    		if(model.getBoxFeeUrl() != null && !model.getBoxFeeUrl().isEmpty()) {
//    			s3Service.deleteFile(model.getBoxFeeUrl());
//    			model.setBoxFeeUrl("NONE");
//    		} 
//    		if(model.getBoxTimeTableUrl() != null && !model.getBoxTimeTableUrl().isEmpty()) {
//    			s3Service.deleteFile(model.getBoxTimeTableUrl());
//    			model.setBoxTimeTableUrl("NONE");
//    		}
//    		boxService.updateBox(model);
//		} catch (Exception e) {
//			res.setMessage("File upload failed: " + e.getMessage());
//			return res;
//		}
//    	
//    	res = boxService.updateBox(model);
//    	
//		return res;
//    }
	
	/*
	@RequestMapping("/Box/deleteBox")
    public ResponseModel deleteBox(@RequestBody BoxModel model) {
    	ResponseModel res = boxService.deleteBox(model);
    	return res;
    }
    */
}
