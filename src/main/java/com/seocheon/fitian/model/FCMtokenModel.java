package com.seocheon.fitian.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FCMtokenModel {

	private String uid;
	private String device_id;
	private String token;
	private String update_at;
	private List<FCMtokenModel> FCMtokenModelList;
}
