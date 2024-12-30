package com.seocheon.fitian.model;

import lombok.Data;

@Data
public class ResponseModel {

	private MemberModel MemberModel;
	private WodModel wodModel;
	private BoxModel boxModel;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
