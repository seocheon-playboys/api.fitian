package com.seocheon.fitian.dto;

import com.seocheon.fitian.model.ArticleModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ArticleSummaryDto {

	@Schema(description = "아티클 넘버")
	private int articleNo;
	
	@Schema(description = "아티클 제목")
	private String articleTitle;
	
	@Schema(description = "작성 날짜")
	private String postDate;
	
	@Schema(description = "회원 uid")
	private String uid;
	
	@Schema(description = "카테고리")
	private String category;
	
	@Schema(description = "작성자")
	private String name;
	
	@Schema(description = "작성자 등급")
	private String rank;
	
	public static ArticleSummaryDto from(ArticleModel m) {
		return new ArticleSummaryDto(
					m.getArticleNo(),
					m.getArticleTitle(),
					m.getPostDate(),
					m.getUid(),
					m.getCategory(),
					m.getName(),
					m.getRank()
				);
	}
}
