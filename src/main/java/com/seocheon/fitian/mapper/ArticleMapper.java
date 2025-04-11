package com.seocheon.fitian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.seocheon.fitian.model.ArticleModel;

@Mapper
public interface ArticleMapper {

	ArticleModel getArticle(ArticleModel model);
	
	List<String> getArticleImg(ArticleModel model);
	
	List<ArticleModel> getArticleList(ArticleModel model);
	
	void createArticle(ArticleModel model);	
	void createArticleImg(ArticleModel model);
	int getLastId();
	
	void updateArticle(ArticleModel model);
	void updateArticleImg(ArticleModel model);
	
	boolean existsById(ArticleModel model);
	void deleteArticle(ArticleModel model);
	void deleteArticleImg(ArticleModel model);
	
}
