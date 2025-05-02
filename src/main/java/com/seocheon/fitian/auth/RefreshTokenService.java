package com.seocheon.fitian.auth;

import org.springframework.stereotype.Service;

import com.seocheon.fitian.mapper.CommonMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final CommonMapper commonMapper;
	
	public void create(String uid, String token) {
		commonMapper.createRefreshToken(uid, token);
	}
	
	public String findByUid(String uid) {
		return commonMapper.getRefreshToken(uid);
	}
	
	public void delete(String uid) {
		commonMapper.deleteRefreshToken(uid);
	}
}
