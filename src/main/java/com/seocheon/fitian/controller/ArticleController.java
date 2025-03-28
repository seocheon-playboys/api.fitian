package com.seocheon.fitian.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.seocheon.fitian.model.ArticleModel;
import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.service.ArticleService;
import com.seocheon.fitian.service.S3Service;


@RestController
public class ArticleController {

	@Autowired
	private ArticleService sv;
	private S3Service s3Sv;
	
	@RequestMapping("/article/getArticle")
    public ResponseModel getArticle(@RequestBody ArticleModel model) {
    	ResponseModel res = sv.getArticle(model);
    	return res;
    }
	
	@RequestMapping("/article/getArticleList")
    public ResponseModel getArticleList(@RequestBody ArticleModel model) {
    	ResponseModel res = sv.getArticleList(model);
    	return res;
    }
	
	@RequestMapping("/article/createArticle")
    public ResponseModel createArticle(@RequestBody ArticleModel model) {
    	ResponseModel res = sv.createArticle(model);
    	return res;
    }
	
	@RequestMapping("/article/createArticleWithImage")
    public ResponseModel createArticleWithImage(
    		@RequestPart(value = "ArticleModel") ArticleModel model,
    		@RequestPart(value = "images", required = false) MultipartFile[] images) throws IOException {
		//images 장수 제한
		if (images.length > 6) {
			ResponseModel res = new ResponseModel();
			res.setMessage("Error: Cannot upload more than 15 files at a time.");
        	return res;
    	} else {
    		//images.s3service -> 파일명 받아오기
    		List<String> fileNames = s3Sv.uploadFiles(images);
    		//model.set파일명
    		model.setFileNames(fileNames);
    		//model.createArticle
        	ResponseModel res = sv.createArticle(model);
        	return res;
    	}
    }
}
