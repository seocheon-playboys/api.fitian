package com.seocheon.fitian.dto;

import java.util.List;

import org.checkerframework.checker.units.qual.m;

import com.seocheon.fitian.model.ArticleModel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleResponseDto {
	private String uid;
	private String articleTitle;
	private String articleScript;
	private String category;
	private String postDate;
	private String boxCode;
	private String name;
	private String rank;
	private String articleImgUrl;
	private List<String> articleImgUrls;
	
	public static ArticleResponseDto from (ArticleModel m) {
		return new ArticleResponseDto(
				m.getUid(),
				m.getArticleTitle(),
				m.getArticleScript(),
				m.getCategory(),
				m.getPostDate(),
				m.getBoxCode(),
				m.getName(),
				m.getRank(),
				m.getArticleImgUrl(),
				m.getArticleImgUrls()
			);
	}
}
