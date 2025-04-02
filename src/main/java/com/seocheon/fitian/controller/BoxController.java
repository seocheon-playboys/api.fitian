package com.seocheon.fitian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.seocheon.fitian.model.BoxModel;
import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.service.BoxService;
import com.seocheon.fitian.service.S3Service;

@RestController
@CrossOrigin(origins = "http://localhost:8088")
public class BoxController {

	@Autowired
	private BoxService sv;
	
	@Autowired
	private S3Service s3Sv;
	
	@RequestMapping("/box/getBox")
    public ResponseModel getBox(@RequestBody BoxModel model) {
    	ResponseModel res = sv.getBox(model);
    	return res;
    }
	
	@RequestMapping("/box/getBoxList")
    public ResponseModel getBoxList(@RequestBody BoxModel model) {
    	ResponseModel res = sv.getBoxList(model);
    	return res;
    }
	
	@RequestMapping("/box/createBox")
    public ResponseModel createBox(@RequestBody BoxModel model) {
    	ResponseModel res = sv.createBox(model);
    	return res;
    }
	
	@RequestMapping("/box/updateBox")
    public ResponseModel updateBox(@RequestBody BoxModel model) {
    	ResponseModel res = sv.updateBox(model);
    	return res;
    }
	
	@RequestMapping("/box/searchBoxCode")
    public ResponseModel searchBoxCode(@RequestBody BoxModel model) {
    	ResponseModel res = sv.searchBoxCode(model);
    	return res;
    }
	
	@RequestMapping("/box/getAllBoxCode")
    public ResponseModel getAllBoxCode() {
    	ResponseModel res = sv.getAllBoxCode();
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
    			model.setBoxFeeUrl(s3Sv.uploadFile(boxFeeImage));
    		} 
    		if(boxTimeTableImage != null && !boxTimeTableImage.isEmpty()) {
    			model.setBoxTimeTableUrl(s3Sv.uploadFile(boxTimeTableImage));
    		} 
		} catch (Exception e) {
			res.setMessage("File upload failed: " + e.getMessage());
			return res;
		}
    	
    	res = sv.updateBox(model);
    	
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
    			s3Sv.deleteFile(model.getBoxFeeUrl());
    			model.setBoxFeeUrl(s3Sv.uploadFile(boxFeeImage));
    		} 
    		if(boxTimeTableImage != null && !boxTimeTableImage.isEmpty()) {
    			s3Sv.deleteFile(model.getBoxTimeTableUrl());
    			model.setBoxTimeTableUrl(s3Sv.uploadFile(boxTimeTableImage));
    		} 
		} catch (Exception e) {
			res.setMessage("File upload failed: " + e.getMessage());
			return res;
		}
    	
    	res = sv.updateBox(model);
    	
		return res;
    }
	
	@PostMapping(value = "/box/deleteBoxInfoImage")
    public ResponseModel deleteBoxInfoImage(@RequestBody BoxModel model) {
    	ResponseModel res = new ResponseModel();

    	try {
    		if(model.getBoxFeeUrl() != null && !model.getBoxFeeUrl().isEmpty()) {
    			s3Sv.deleteFile(model.getBoxFeeUrl());
    			model.setBoxFeeUrl("NONE");
    		} 
    		if(model.getBoxTimeTableUrl() != null && !model.getBoxTimeTableUrl().isEmpty()) {
    			s3Sv.deleteFile(model.getBoxTimeTableUrl());
    			model.setBoxTimeTableUrl("NONE");
    		}
    		sv.updateBox(model);
		} catch (Exception e) {
			res.setMessage("File upload failed: " + e.getMessage());
			return res;
		}
    	
    	res = sv.updateBox(model);
    	
		return res;
    }
	
	/*
	@RequestMapping("/Box/deleteBox")
    public ResponseModel deleteBox(@RequestBody BoxModel model) {
    	ResponseModel res = sv.deleteBox(model);
    	return res;
    }
    */
}
