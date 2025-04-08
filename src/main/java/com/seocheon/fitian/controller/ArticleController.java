package com.seocheon.fitian.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@Autowired
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
	
	@PostMapping(value = "/article/createArticleWithImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseModel createArticleWithImage(
    		@RequestPart(value = "ArticleModel") ArticleModel model,
    		@RequestPart(value = "ArticleImages", required = false) MultipartFile[] images) throws IOException {
		//images 장수 제한
		if (images.length > 6) {
			ResponseModel res = new ResponseModel();
			res.setMessage("Error: Cannot upload more than 6 files at a time.");
        	return res;
    	} else {
    		//images.s3service -> 파일명 받아오기
    		List<String> ImageUrls = s3Sv.uploadFiles(images);
    		//model.set파일명
    		model.setArticleImgUrls(ImageUrls);
    		//model.createArticle
        	ResponseModel res = sv.createArticleWithImage(model);
        	return res;
    	}
    }
	
	@RequestMapping("/article/deleteArticle")
    public ResponseModel deleteArticle(@RequestBody ArticleModel model) {
		ResponseModel res = new ResponseModel();
		List<String> articleImgUrls = sv.getArticleimgUrls(model).getArticleModel().getArticleImgUrls();
		try {
			for(int i = 0; i < articleImgUrls.size(); i++) {
				s3Sv.deleteFile(articleImgUrls.get(i));
			}
		} catch(Exception e) {
			res.setMessage("이미지 삭제에 실패했습니다.");
		}
		res = sv.deleteArticle(model);
    	return res;
    }
}
