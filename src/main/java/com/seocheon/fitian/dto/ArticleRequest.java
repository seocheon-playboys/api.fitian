package com.seocheon.fitian.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "아티클 생성 요청 DTO")
public class ArticleRequest {
	
	@Schema(description = "작성자 uid")
	private String uid;
	
	@Schema(description = "아티클 제목")
	private String articleTitle;
	
	@Schema(description = "아티클 내용")
	private String articleScript;
	
	@Schema(description = "카테고리")
	private String category;
	
	@Schema(description = "박스 코드")
	private String boxCode;
	
	@Schema(description = "아티클 이미지 url")
	private String articleImgUrl;
	
	@Schema(description = "아티클 이미지 urls")
	private List<String> articleImgUrls;
}
