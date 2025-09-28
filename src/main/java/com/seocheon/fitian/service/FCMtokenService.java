package com.seocheon.fitian.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seocheon.fitian.dto.FCMtokenCreateRequest;
import com.seocheon.fitian.mapper.FCMtokenMapper;
import com.seocheon.fitian.model.FCMtokenModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMtokenService {

	private final FCMtokenMapper mapper;
	
	public FCMtokenModel getToken(FCMtokenModel model) {
		return mapper.getToken(model);
	}

	@Transactional
	public void createToken(FCMtokenModel model) {
		mapper.createToken(model);
	}
	
	@Transactional
	public void deleteToken(String uid) {
		mapper.deleteToken(uid);
	}
	
	@Transactional
	public void upsertToken(String uid, FCMtokenCreateRequest req) {
		FCMtokenModel checkToken = mapper.getTokenByDeviceId(req);

		if(checkToken!=null&&!checkToken.getUid().equals(uid)) { //같은 deviceID에 다른 uid일 경우 기존 토큰 삭제
			mapper.deleteToken(checkToken.getToken());
		}

		FCMtokenModel model = FCMtokenModel.builder()
				.uid(uid)
				.device_id(req.getDevice_id())
				.token(req.getToken())
				.is_active(true)
				.build();
		
		int rows = mapper.upsert(model);
		log.debug("FCM token upsert, uid={}, device_id={}, rows={}", uid, req.getDevice_id(), rows);
	}
	
	public void deactivateToken(String token) {
		int rows = mapper.deactivate(token);
		log.debug("Deactivated FCM token = {}, rows ={}", token, rows);
	}
	
	@Scheduled(cron = "0 0 3 * * *")
	public void pruneInactiveTokens() {
		int deleted = mapper.deleteInactiveOlderThan(30);
		if (deleted > 0) {
			log.info("Pruned {} inactive FCM tokens", deleted);
		}
	}
}
