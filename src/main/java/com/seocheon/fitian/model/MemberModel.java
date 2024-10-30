package com.seocheon.fitian.model;

import java.util.List;

public class MemberModel {

	private String uid;
	private String name;
	private String nickname;
	private String mainBox;
	private String phoneNo;
	private String rank;
	private String gender;
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
	public String getMainBox() {
		return mainBox;
	}
	public void setMainBox(String mainBox) {
		this.mainBox = mainBox;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
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
	public List<MembershipModel> getMembershipList() {
		return membershipList;
	}
	public void setMembershipList(List<MembershipModel> membershipList) {
		this.membershipList = membershipList;
	}
	
	
}
