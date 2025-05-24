package com.seocheon.fitian.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FCMtokenModel {

	private String uid;
	private String device_id;
	private String token;
	private Boolean is_active;
	private String update_at;
	private List<FCMtokenModel> FCMtokenModelList;
}
