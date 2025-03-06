package com.seocheon.fitian.model;

import java.util.List;

import lombok.Data;

@Data
public class ResponseModel {

	private MemberModel MemberModel;
	private List<MemberModel> MemberModelList;
	private WodModel wodModel;
	private BoxModel boxModel;
	private List<BoxModel> boxModelList;
	private RecordModel recordModel;
	private List<RecordModel> recordModelList;
	private ArticleModel articleModel;
	private List<ArticleModel> articleModelList;
	private String[] BoxCodeList;
	private String message;

	public MemberModel getMemberModel() {
		return MemberModel;
	}

	public void setMemberModel(MemberModel MemberModel) {
		this.MemberModel = MemberModel;
	}

	public List<MemberModel> getMemberModelList() {
		return MemberModelList;
	}

	public void setMemberModelList(List<MemberModel> memberModelList) {
		MemberModelList = memberModelList;
	}

	public WodModel getWodModel() {
		return wodModel;
	}

	public void setWodModel(WodModel wodModel) {
		this.wodModel = wodModel;
	}

	public BoxModel getBoxModel() {
		return boxModel;
	}

	public void setBoxModel(BoxModel boxModel) {
		this.boxModel = boxModel;
	}

	public List<BoxModel> getBoxModelList() {
		return boxModelList;
	}

	public void setBoxModelList(List<BoxModel> boxModelList) {
		this.boxModelList = boxModelList;
	}

	public RecordModel getRecordModel() {
		return recordModel;
	}

	public void setRecordModel(RecordModel recordModel) {
		this.recordModel = recordModel;
	}

	public List<RecordModel> getRecordModelList() {
		return recordModelList;
	}

	public void setRecordModelList(List<RecordModel> recordModelList) {
		this.recordModelList = recordModelList;
	}

	public ArticleModel getArticleModel() {
		return articleModel;
	}

	public void setArticleModel(ArticleModel articleModel) {
		this.articleModel = articleModel;
	}

	public List<ArticleModel> getArticleModelList() {
		return articleModelList;
	}

	public void setArticleModelList(List<ArticleModel> articleModelList) {
		this.articleModelList = articleModelList;
	}

	public String[] getBoxCodeList() {
		return BoxCodeList;
	}

	public void setBoxCodeList(String[] boxCodeList) {
		BoxCodeList = boxCodeList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
