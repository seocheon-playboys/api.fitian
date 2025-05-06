package com.seocheon.fitian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seocheon.fitian.model.MemberModel;

@Mapper
public interface MemberMapper {
	
	/**
     * 회원 정보 가져오기
	 * @param 
     */
	List<MemberModel> getAllMember(MemberModel member);
	
	MemberModel findByUid(@Param("uid") String uid);
	
	void joinMember(MemberModel member);
	
	//void insertMember(MemberModel member);
	
	void updateMember(MemberModel member);
	
	int deleteMember(String uid);
	
}
