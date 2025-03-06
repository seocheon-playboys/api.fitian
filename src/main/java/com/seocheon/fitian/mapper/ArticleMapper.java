package com.seocheon.fitian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.seocheon.fitian.model.ArticleModel;

@Mapper
public interface ArticleMapper {

	ArticleModel getArticle(ArticleModel model);
	
	List<ArticleModel> getArticleList(ArticleModel model);
	
	void createArticle(ArticleModel model);
	
	void updateArticle(ArticleModel model);
	
}
