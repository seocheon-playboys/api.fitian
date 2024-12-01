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
	List<Map<String, Object>> getUsers();
	
	MemberModel getMember(MemberModel member);
	
	MemberModel joinMember(MemberModel member);
	
	MemberModel updateMember(MemberModel member);
	
	MemberModel deleteMember(MemberModel member);
	
	String getTest(String test);
}
