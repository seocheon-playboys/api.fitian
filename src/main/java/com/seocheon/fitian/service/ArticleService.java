package com.seocheon.fitian.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seocheon.fitian.dto.ArticleListRequest;
import com.seocheon.fitian.dto.ArticleRequest;
import com.seocheon.fitian.dto.ArticleSummaryDto;
import com.seocheon.fitian.mapper.ArticleMapper;
import com.seocheon.fitian.model.ArticleModel;
import com.seocheon.fitian.model.ResponseModel;

@Service
public class ArticleService {
	private final ArticleMapper mapper;
	
	public ArticleService(ArticleMapper mapper) {
		this.mapper = mapper;
	}
	
	public ArticleModel getArticle(int articleNo) {
		
		ArticleModel article = mapper.getArticle(articleNo);
		
		article.setArticleImgUrls(mapper.getArticleImg(articleNo));
		
		return article;
	}
	
	public ArticleModel getIntroduction(String boxCode) {
		
		ArticleModel article = mapper.getIntroduction(boxCode);
		
		return article;
	}
	
	public ResponseModel getArticleimgUrls(ArticleModel model) {
		
		ResponseModel res = new ResponseModel();
		
		model.setArticleImgUrls(mapper.getArticleImg(model.getArticleNo()));
		
		res.setArticleModel(model);
		
		return res;
	}

	public List<ArticleSummaryDto> getArticleList(ArticleListRequest request) {
		
		List<ArticleModel> articleModelList = mapper.getArticleList(request);
		
		List<ArticleSummaryDto> articleList = articleModelList.stream()
				.map(ArticleSummaryDto::from)
				.collect(Collectors.toList());
		
		return articleList;
	}
	
	@Transactional
	public void createArticle(ArticleRequest request) {
		
		mapper.createArticle(request);
		
	}
	
	@Transactional
	public ResponseModel updateArticle(ArticleModel model) {
		
		ResponseModel res = new ResponseModel();
		
		try {
			mapper.updateArticle(model);
			
			String url = model.getArticleImgUrl();
			if(url != null && !url.trim().isEmpty()) { // url이 있다면 이전 이미지url db에서 삭제
				mapper.deleteArticleImg(model);
			}
			
			if(model.getArticleImgUrls() != null && model.getArticleImgUrls().toString() != "") { // 신규 이미지가 있다면 이미지url db에 등록
				List<String> ImageUrls = model.getArticleImgUrls();
				for(int i = 0; i<ImageUrls.size(); i++) {
					model.setArticleImgUrl(ImageUrls.get(i));
					//글의 id와 url을 db에 등록
					//한 장 일 경우만 가능. 추후 바꿔야함
					mapper.createArticleImg(model);
				}
			}
			res.setMessage("글이 수정되었습니다.");
		} catch(Exception e) {
			res.setMessage("글 수정에 실패했습니다.");
		}
		return res;
	}
	
	@Transactional
	public void deleteArticle(ArticleModel model) {
		if(mapper.existsById(model)) {	
			mapper.deleteArticle(model);
			mapper.deleteArticleImg(model);
		}
	}
	
	@Transactional
	public void createArticleWithImage(ArticleRequest request) {
		
		//글만 등록
		mapper.createArticle(request);
		
		ArticleModel model = new ArticleModel();
		//등록된 글의 id 가져오기
		model.setArticleNo(mapper.getLastId());
		List<String> ImageUrls = request.getArticleImgUrls();
		for(int i = 0; i<ImageUrls.size(); i++) {
			model.setArticleImgUrl(ImageUrls.get(i));
			//글의 id와 url을 db에 등록
			mapper.createArticleImg(model);
		}

	}
	
}
