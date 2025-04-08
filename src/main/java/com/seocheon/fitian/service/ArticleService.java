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
		
		article.setArticleImgUrls(mapper.getArticleImg(model));
		
		res.setArticleModel(article);
		
		return res;
	}
	
public ResponseModel getArticleimgUrls(ArticleModel model) {
		
		ResponseModel res = new ResponseModel();
		
		model.setArticleImgUrls(mapper.getArticleImg(model));
		
		res.setArticleModel(model);
		
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
	
	public ResponseModel deleteArticle(ArticleModel model) {
		
		ResponseModel res = new ResponseModel();
		
		try {
			mapper.deleteArticle(model);
			mapper.deleteArticleImg(model);
			res.setMessage("글이 삭제되었습니다.");
		} catch(Exception e) {
			res.setMessage("글 삭제에 실패했습니다.");
		}
		return res;
	}
	
	public ResponseModel createArticleWithImage(ArticleModel model) {
		
		ResponseModel res = new ResponseModel();
		
		try {
			//글만 등록
			mapper.createArticle(model);
			//등록된 글의 id 가져오기
			model.setArticleNo(mapper.getLastId());
			List<String> ImageUrls = model.getArticleImgUrls();
			for(int i = 0; i<ImageUrls.size(); i++) {
				model.setArticleImgUrl(ImageUrls.get(i));
				//글의 id와 url을 db에 등록
				mapper.createArticleImg(model);
			}
			res.setMessage("글이 등록되었습니다.");
		} catch(Exception e) {
			res.setMessage("글 등록에 실패했습니다.");
		}
		return res;
	}
	
}
