package com.seocheon.fitian.model;

import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FCMtokenModel {

	private String uid;
	private String device_id;
	private String token;
	private Boolean is_active;
	private String update_at;
	private List<FCMtokenModel> FCMtokenModelList;
}
