package com.seocheon.fitian.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.seocheon.fitian.dto.BoxResponseDto;
import com.seocheon.fitian.dto.BoxSummaryDto;
import com.seocheon.fitian.model.ApiResponse;
import com.seocheon.fitian.model.BoxModel;
import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.service.BoxService;
import com.seocheon.fitian.service.S3Service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

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
	@PostMapping("/getBox")
    public ResponseEntity<ApiResponse<BoxResponseDto>> getBox(
    		@Parameter(description="해당 박스 코드", required = false)
    		@RequestParam(value="boxCode", required = false) String boxCode,
    		
    		@Parameter(description="해당 박스 이름", required = false)
    		@RequestParam(value="boxName", required = false) String boxName) {
    	
		if((boxCode == null || boxCode.isBlank()) && (boxName == null || boxName.isBlank())) {
			return ResponseEntity.badRequest().body(ApiResponse.error("boxCode 또는 boxName 중 하나는 반드시 필요합니다."));
		}
		
		BoxModel box = boxService.getBoxByCodeOrName(boxCode);
    	return ResponseEntity.ok(ApiResponse.success(BoxResponseDto.from(box)));
    }
	
	//박스 리스트 가져오기
	@Operation(summary = "박스 요약 목록 가져오기", description = "boxCode 또는 boxName으로 박스 정보를 반환합니다.",
			security = {} )
	@GetMapping("/getBoxList")
    public ResponseEntity<ApiResponse<List<BoxSummaryDto>>> getBoxList() {
		//일정 갯수 추가 필요
		List<BoxSummaryDto> summaries = boxService.getBoxList();
    	return ResponseEntity.ok(ApiResponse.success(summaries));
    }
	
	@PostMapping("/box/createBox")
    public ResponseModel createBox(@RequestBody BoxModel model) {
    	ResponseModel res = boxService.createBox(model);
    	return res;
    }
	
	@RequestMapping("/box/updateBox")
    public ResponseModel updateBox(@RequestBody BoxModel model) {
    	ResponseModel res = boxService.updateBox(model);
    	return res;
    }
	
	@RequestMapping("/box/searchBoxCode")
    public ResponseModel searchBoxCode(@RequestBody BoxModel model) {
    	ResponseModel res = boxService.searchBoxCode(model);
    	return res;
    }
	
	@RequestMapping("/box/getAllBoxCode")
    public ResponseModel getAllBoxCode() {
    	ResponseModel res = boxService.getAllBoxCode();
    	return res;
    }
	
	@RequestMapping("/box/getBoxImages")
    public ResponseModel getBoxImages(@RequestBody BoxModel model) {
    	ResponseModel res = boxService.getBoxImages(model);
    	return res;
    }
	
	@PostMapping(value = "/box/addBoxInfoImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseModel addBoxInfoImage(
    		@RequestPart(value = "boxFeeImage", required = false) MultipartFile boxFeeImage,
    		@RequestPart(value = "boxTimeTableImage", required = false) MultipartFile boxTimeTableImage,
    		@RequestPart(value = "BoxModel") BoxModel model) {
    	ResponseModel res = new ResponseModel();

    	try {
    		if(boxFeeImage != null && !boxFeeImage.isEmpty()) {
    			model.setBoxFeeUrl(s3Service.uploadFile(boxFeeImage));
    		} 
    		if(boxTimeTableImage != null && !boxTimeTableImage.isEmpty()) {
    			model.setBoxTimeTableUrl(s3Service.uploadFile(boxTimeTableImage));
    		} 
		} catch (Exception e) {
			res.setMessage("File upload failed: " + e.getMessage());
			return res;
		}
    	
    	res = boxService.updateBox(model);
    	res.setBoxModel(model);
		return res;
    }
	
	@PostMapping(value = "/box/updateBoxInfoImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseModel updateBoxInfoImage(
    		@RequestPart(value = "boxFeeImage", required = false) MultipartFile boxFeeImage,
    		@RequestPart(value = "boxTimeTableImage", required = false) MultipartFile boxTimeTableImage,
    		@RequestPart(value = "BoxModel") BoxModel model) {
    	ResponseModel res = new ResponseModel();

    	try {
    		if(boxFeeImage != null && !boxFeeImage.isEmpty()) {
    			s3Service.deleteFile(model.getBoxFeeUrl());
    			model.setBoxFeeUrl(s3Service.uploadFile(boxFeeImage));
    		} 
    		if(boxTimeTableImage != null && !boxTimeTableImage.isEmpty()) {
    			s3Service.deleteFile(model.getBoxTimeTableUrl());
    			model.setBoxTimeTableUrl(s3Service.uploadFile(boxTimeTableImage));
    		} 
		} catch (Exception e) {
			res.setMessage("File upload failed: " + e.getMessage());
			return res;
		}
    	
    	res = boxService.updateBox(model);
    	res.setBoxModel(model);
		return res;
    }
	
	@PostMapping(value = "/box/deleteBoxInfoImage")
    public ResponseModel deleteBoxInfoImage(@RequestBody BoxModel model) {
    	ResponseModel res = new ResponseModel();

    	try {
    		if(model.getBoxFeeUrl() != null && !model.getBoxFeeUrl().isEmpty()) {
    			s3Service.deleteFile(model.getBoxFeeUrl());
    			model.setBoxFeeUrl("NONE");
    		} 
    		if(model.getBoxTimeTableUrl() != null && !model.getBoxTimeTableUrl().isEmpty()) {
    			s3Service.deleteFile(model.getBoxTimeTableUrl());
    			model.setBoxTimeTableUrl("NONE");
    		}
    		boxService.updateBox(model);
		} catch (Exception e) {
			res.setMessage("File upload failed: " + e.getMessage());
			return res;
		}
    	
    	res = boxService.updateBox(model);
    	
		return res;
    }
	
	/*
	@RequestMapping("/Box/deleteBox")
    public ResponseModel deleteBox(@RequestBody BoxModel model) {
    	ResponseModel res = boxService.deleteBox(model);
    	return res;
    }
    */
}
