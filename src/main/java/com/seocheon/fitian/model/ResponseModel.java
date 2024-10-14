package com.seocheon.fitian.model;

import lombok.Data;

@Data
public class ResponseModel {

	private UserModel userModel;

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}
	
}
