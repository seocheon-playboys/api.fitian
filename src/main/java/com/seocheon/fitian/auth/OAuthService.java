package com.seocheon.fitian.auth;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.seocheon.fitian.auth.dto.OAuthUserInfo;
import com.seocheon.fitian.mapper.MemberMapper;
import com.seocheon.fitian.model.MemberModel;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuthService {

	private final KakaoApiClient kakaoApiClient;
	private final AppleApiClient appleApiClient;
	private final MemberMapper memberMapper;
	
	
	public MemberModel findOrCreateUserBySocialToken(String token, String provider) {
	
		OAuthUserInfo userInfo = getUserInfoFromProvider(token, provider);
		
		String uid = userInfo.getId();
		
		MemberModel member = new MemberModel();
		member.setUid(uid);
		
		MemberModel existing = memberMapper.findByUid(member.getUid());
		if(existing != null) return existing;
		
		member.setName(userInfo.getNickname() != null ? userInfo.getNickname() : "");
		member.setEmail(userInfo.getEmail());
		member.setJoinDate(LocalDateTime.now().toString());
		
		memberMapper.joinMember(member);
		
		return member;
	}
	
	private OAuthUserInfo getUserInfoFromProvider(String token, String provider) {
		return switch (provider.toLowerCase()) {
			case "kakao" -> kakaoApiClient.getUserInfo(token);
			case "apple" -> appleApiClient.getUserInfo(token);
			default -> throw new IllegalArgumentException("지원하지 않는 방식 : "+provider);
		};
	}
}
