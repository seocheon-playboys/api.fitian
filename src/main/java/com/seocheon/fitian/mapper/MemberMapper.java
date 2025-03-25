package com.seocheon.fitian.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.seocheon.fitian.model.MemberModel;

@Mapper
public interface MemberMapper {
	
	/**
     * 회원 정보 가져오기
	 * @param 
     */
	List<MemberModel> getAllMember(MemberModel member);
	
	MemberModel getMember(MemberModel member);
	
	void joinMember(MemberModel member);
	
	void updateMember(MemberModel member);
	
	void deleteMember(MemberModel member);
	
	int existMember(MemberModel member);
	
	String getTest(String test);
}
