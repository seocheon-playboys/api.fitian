package com.seocheon.fitian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.seocheon.fitian.model.FCMtokenModel;
import com.seocheon.fitian.model.MemberModel;

@Mapper
public interface FCMtokenMapper {

	FCMtokenModel getToken(FCMtokenModel model);
	List<String> getTokens(MemberModel model);
	void createToken(FCMtokenModel model);
	void deleteToken(String token);
	
	int upsert(FCMtokenModel model);
	int deactivate(String token);
	List<FCMtokenModel> findByUid(String uid);
	int deleteInactiveOlderThan(int days);
}
