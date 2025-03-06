package com.seocheon.fitian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seocheon.fitian.model.ArticleModel;
import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.service.ArticleService;


@RestController
public class ArticleController {

	@Autowired
	private ArticleService sv;
	
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
}
