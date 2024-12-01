package com.seocheon.fitian.model;

import lombok.Data;

@Data
public class ResponseModel {

	private MemberModel MemberModel;
	private WodModel wodModel;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
