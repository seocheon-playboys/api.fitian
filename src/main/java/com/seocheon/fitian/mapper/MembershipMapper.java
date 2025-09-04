package com.seocheon.fitian.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seocheon.fitian.model.MembershipModel;

@Mapper
public interface MembershipMapper {
	
	MembershipModel findByUid(@Param("boxCode") String boxCode ,@Param("uid") String uid);
	
	void createMembership(MembershipModel model);
	
	int updateMembership(MembershipModel model);
	
	void deleteMembership (MembershipModel model);

	int expireMemberships();
	
}
