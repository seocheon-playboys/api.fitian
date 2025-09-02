package com.seocheon.fitian.model;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
	private int membershipNo;
	private LocalDate expirationDate;
}
