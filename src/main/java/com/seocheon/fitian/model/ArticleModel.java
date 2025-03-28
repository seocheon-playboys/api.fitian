package com.seocheon.fitian.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleModel {

	private int articleNo;
	private String uid;
	private String articleTitle;
	private String articleScript;
	private String category;
	private String postDate;
	private String boxCode;
	private String name;
	private String rank;
	private List<String> fileNames;
	private List<ArticleModel> articleModelList;
	
}
