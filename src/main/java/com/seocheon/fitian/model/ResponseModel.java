package com.seocheon.fitian.model;

import java.util.List;

import lombok.Data;

@Data
public class ResponseModel {

	private MemberModel MemberModel;
	private WodModel wodModel;
	private BoxModel boxModel;
	private RecordModel recordModel;
	private List<RecordModel> recordModelList;
	private String message;

	public MemberModel getMemberModel() {
		return MemberModel;
	}

	public void setMemberModel(MemberModel MemberModel) {
		this.MemberModel = MemberModel;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
