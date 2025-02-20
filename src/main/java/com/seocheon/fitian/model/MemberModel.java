package com.seocheon.fitian.model;

import java.util.List;

import lombok.Data;

@Data
public class MemberModel {

	private String uid;	
	private String name;
	private String nickname;
	private String boxName;
	private String email;
	private String rank;
	private String gender;
	private String boxCode;
	private List<MembershipModel> membershipList;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getBoxName() {
		return boxName;
	}
	public void setBoxName(String boxName) {
		this.boxName = boxName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBoxCode() {
		return boxCode;
	}
	public void setBoxCode(String boxCode) {
		this.boxCode = boxCode;
	}
	public List<MembershipModel> getMembershipList() {
		return membershipList;
	}
	public void setMembershipList(List<MembershipModel> membershipList) {
		this.membershipList = membershipList;
	}
	
	
}
