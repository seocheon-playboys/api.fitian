package com.seocheon.fitian.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.Notification;
import com.seocheon.fitian.mapper.FCMtokenMapper;
import com.seocheon.fitian.model.MemberModel;
import com.seocheon.fitian.model.PushModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor 
public class PushService {
	
	private final FCMtokenMapper tokenMapper;

	public void notifyUser(MemberModel member, PushModel pushModel) throws Exception {
		List<String> tokens = fetchTokens(member);
		
		if(tokens.isEmpty()) {
			log.debug("No token for user {}", member.getUid());
			return;
		}
		
		for (String token : tokens) {
			sendSingle(token, pushModel);
		}
	}
	
	private void sendSingle(String token, PushModel pushModel) {
		Message msg = Message.builder()
				.setToken(token)
				.setNotification(Notification.builder()
						.setTitle(pushModel.getTitle())
						.setBody(pushModel.getBody())
						.build())
				.putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
				.build();
		try {
			String id = FirebaseMessaging.getInstance().send(msg);
			log.debug("Sent push to {}: {}", token, id);
		} catch (FirebaseMessagingException ex) {
			log.error("Failed to send to {}: {}", token, ex.getMessage());
			handleFailure(token, ex);
		}
	}
	
	@Transactional
	private void handleFailure(String token, FirebaseMessagingException ex) {
		MessagingErrorCode code = ex.getMessagingErrorCode();
		
		switch(code) {
			case INVALID_ARGUMENT,
				UNREGISTERED:    
				tokenMapper.deleteToken(token);
			
			default:
				log.warn("FCM send failed ({}): {}", code, ex.getMessage());
		}
		
	}

	private List<String> fetchTokens(MemberModel model) {
		
		List<String> tokens = tokenMapper.getTokens(model);
		
		return tokens;
	}
}
