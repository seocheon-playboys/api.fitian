package com.seocheon.fitian.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "아티클 조회 요청 DTO")
public class ArticleListRequest {
	
	@Schema(description = "박스 코드")
	private String boxCode;
	
	@Schema(description = "카테고리")
	private String category;
	
}
