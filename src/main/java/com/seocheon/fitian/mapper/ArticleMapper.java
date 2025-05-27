package com.seocheon.fitian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seocheon.fitian.dto.ArticleListRequest;
import com.seocheon.fitian.dto.ArticleRequest;
import com.seocheon.fitian.model.ArticleModel;

@Mapper
public interface ArticleMapper {

	ArticleModel getArticle(@Param("articleNo") int articleNo);
	
	ArticleModel getIntroduction(@Param("boxCode") String boxCode);
	
	List<String> getArticleImg(@Param("articleNo") int articleNo);
	
	List<ArticleModel> getArticleList(ArticleListRequest request);
	
	void createArticle(ArticleRequest request);	
	void createArticleImg(ArticleModel model);
	int getLastId();
	
	void updateArticle(ArticleModel model);
	void updateArticleImg(ArticleModel model);
	
	boolean existsById(ArticleModel model);
	void deleteArticle(ArticleModel model);
	void deleteArticleImg(ArticleModel model);
	
}
