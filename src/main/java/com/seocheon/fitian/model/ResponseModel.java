package com.seocheon.fitian.model;

import java.util.List;

import lombok.Data;

@Data
public class ResponseModel {

	private MemberModel MemberModel;
	private List<MemberModel> MemberModelList;
	private WodModel wodModel;
	private BoxModel boxModel;
	private RecordModel recordModel;
	private List<RecordModel> recordModelList;
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
