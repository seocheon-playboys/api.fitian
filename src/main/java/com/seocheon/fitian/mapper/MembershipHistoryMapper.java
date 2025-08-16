package com.seocheon.fitian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seocheon.fitian.model.MembershipHistoryModel;
import com.seocheon.fitian.model.MembershipModel;

@Mapper
public interface MembershipHistoryMapper {
	
	List<MembershipHistoryModel> findByUid(@Param("membershipNo") int membershipNo);
	
	void createHistory(MembershipHistoryModel model);
	
}
