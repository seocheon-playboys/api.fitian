package com.seocheon.fitian.model;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberModel {

	private String uid;	
	private String name;
	private String nickname;
	private String boxName;
	private String email;
	private String rank;
	private String gender;
	private String boxCode;
	private String useYN;
	private String joinDate;
	private List<MembershipModel> membershipList;
	
}
