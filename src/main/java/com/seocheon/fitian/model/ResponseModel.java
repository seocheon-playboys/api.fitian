package com.seocheon.fitian.model;

import lombok.Data;

@Data
public class ResponseModel {

	private MemberModel userModel;

	public MemberModel getUserModel() {
		return userModel;
	}

	public void setUserModel(MemberModel userModel) {
		this.userModel = userModel;
	}
	
}
