package com.seocheon.fitian.model;

import java.util.List;

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
	private List<ArticleModel> articleModelList;
	
	public int getArticleNo() {
		return articleNo;
	}
	public void setArticleNo(int articleNo) {
		this.articleNo = articleNo;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getArticleTitle() {
		return articleTitle;
	}
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	public String getArticleScript() {
		return articleScript;
	}
	public void setArticleScript(String articleScript) {
		this.articleScript = articleScript;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPostDate() {
		return postDate;
	}
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}
	public String getBoxCode() {
		return boxCode;
	}
	public void setBoxCode(String boxCode) {
		this.boxCode = boxCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public List<ArticleModel> getArticleModelList() {
		return articleModelList;
	}
	public void setArticleModelList(List<ArticleModel> articleModelList) {
		this.articleModelList = articleModelList;
	}
}
