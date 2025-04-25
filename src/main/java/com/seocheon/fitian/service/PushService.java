package com.seocheon.fitian.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import com.seocheon.fitian.mapper.FCMtokenMapper;
import com.seocheon.fitian.model.FCMtokenModel;
import com.seocheon.fitian.model.MemberModel;
import com.seocheon.fitian.model.PushModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor 
public class PushService {
	
	private final FCMtokenMapper tokenMapper;
	private static final int CHUNK = 20;

	public void notifyUser(MemberModel member, PushModel pushModel) throws Exception {
		List<String> tokens = fetchTokens(member);
		
		if(tokens.isEmpty()) {
			log.debug("No token for user {}", member.getUid());
			return;
		}
		
		if(tokens.size() == 1) {
			sendSingle(tokens.get(0), pushModel);
			return;
		} else {
			sendMulticast(tokens, pushModel);
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
			System.out.println("FCM sent : {} "+id);
		} catch (FirebaseMessagingException ex) {
			handleFailure(token, ex);
		}
	}
	
	private void sendMulticast(List<String> tokens, PushModel pushModel) throws Exception {
		for ( int i = 0; i < tokens.size(); i += CHUNK ) {
			List<String> slice = tokens.subList(i, Math.min(i + CHUNK, tokens.size()));
			
			MulticastMessage mm = MulticastMessage.builder()
					.addAllTokens(slice)
					.setNotification(Notification.builder()
							.setTitle(pushModel.getTitle())
							.setBody(pushModel.getBody())
							.build())
					.putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
					.build();
			
			BatchResponse resp = FirebaseMessaging.getInstance().sendMulticast(mm);
			
			int idx = 0;
			for ( SendResponse r : resp.getResponses()) {
				if(!r.isSuccessful()) {
					handleFailure(slice.get(idx), r.getException());
				}
			}
		}
	}

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
