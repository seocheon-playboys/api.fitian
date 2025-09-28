package com.seocheon.fitian.mapper;

import java.util.List;

import com.seocheon.fitian.dto.FCMtokenCreateRequest;
import org.apache.ibatis.annotations.Mapper;

import com.seocheon.fitian.model.FCMtokenModel;
import com.seocheon.fitian.model.MemberModel;

@Mapper
public interface FCMtokenMapper {

	FCMtokenModel getToken(FCMtokenModel model);
	FCMtokenModel getTokenByDeviceId(FCMtokenCreateRequest req);
	List<String> getTokens(MemberModel model);
	void createToken(FCMtokenModel model);
	void deleteToken(String uid);
	
	int upsert(FCMtokenModel model);
	int deactivate(String token);
	List<FCMtokenModel> findByUid(String uid);
	int deleteInactiveOlderThan(int days);
}
