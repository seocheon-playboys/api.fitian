package com.seocheon.fitian.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.seocheon.fitian.mapper.ArticleMapper;
import com.seocheon.fitian.model.ArticleModel;
import com.seocheon.fitian.model.ResponseModel;

@Service
public class ArticleService {
	private final ArticleMapper mapper;
	
	public ArticleService(ArticleMapper mapper) {
		this.mapper = mapper;
	}
	
	public ResponseModel getArticle(ArticleModel model) {
		
		ResponseModel res = new ResponseModel();
		
		ArticleModel article = mapper.getArticle(model);
		
		res.setArticleModel(article);
		
		return res;
	}

	public ResponseModel getArticleList(ArticleModel model) {
		
		ResponseModel res = new ResponseModel();
		
		List<ArticleModel> articleList = mapper.getArticleList(model);
		
		res.setArticleModelList(articleList);
		
		return res;
	}
	
	public ResponseModel createArticle(ArticleModel model) {
		
		ResponseModel res = new ResponseModel();
		
		try {
			mapper.createArticle(model);
			res.setMessage("글이 등록되었습니다.");
		} catch(Exception e) {
			res.setMessage("글 등록에 실패했습니다.");
		}
		return res;
	}
	
	public ResponseModel updateArticle(ArticleModel model) {
		
		ResponseModel res = new ResponseModel();
		
		try {
			mapper.updateArticle(model);
			res.setMessage("글이 수정되었습니다.");
		} catch(Exception e) {
			res.setMessage("글 수정에 실패했습니다.");
		}
		return res;
	}
	
}
