package com.seocheon.fitian.service;

import java.util.List;

import com.seocheon.fitian.mapper.ChannelMapper;
import com.seocheon.fitian.mapper.CommonMapper;
import com.seocheon.fitian.mapper.FCMtokenMapper;
import com.seocheon.fitian.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.seocheon.fitian.auth.security.CustomUserDetails;
import com.seocheon.fitian.dto.MemberResponseDto;
import com.seocheon.fitian.dto.MembersResponseDto;
import com.seocheon.fitian.dto.UpdateSelfRequest;
import com.seocheon.fitian.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor 
public class MemberService {
	
	private final MemberMapper memberMapper;
	private final PushService pushService;
	private final ChannelMapper channelMapper;
	private final ChannelService channelService;
	private final MembershipService membershipService;
	private final FCMtokenMapper fcMtokenMapper;
	private final CommonMapper commonMapper;
	
	public MemberModel findByUid(String uid) {
		MemberModel member = memberMapper.findByUid(uid);
		return member;
	}
	
	@Transactional
	public MemberResponseDto updateMyInfo(UpdateSelfRequest request, CustomUserDetails userDetails) {
		
		MemberModel member = userDetails.getMember();
		
		if (request.getName() != null && !request.getName().isBlank()) {
	        member.setName(request.getName());
	    }
	    if (request.getEmail() != null && !request.getEmail().isBlank()) {
	        member.setEmail(request.getEmail());
	    }
	    if (request.getBoxName() != null && !request.getBoxName().isBlank()) {
	        member.setBoxName(request.getBoxName());
	    }
	    if (request.getBoxCode() != null && !request.getBoxCode().isBlank()) {
	        member.setBoxCode(request.getBoxCode());
	    }
		
		
		if(request.getBoxCode() != null && !request.getBoxCode().equals(userDetails.getMember().getBoxCode())) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
	            @Override
	            public void afterCommit() {
	                sendNewMemberPush(member);
	            }
	        });
		}
		
		if(userDetails.getMember().getRank() == null) {
			member.setRank("guest");
			
			if (request.getGender() != null && !request.getGender().isBlank()) {
		        member.setGender(request.getGender());
		    }
		}
		
		memberMapper.updateMember(member);
		
		MemberModel updated = memberMapper.findByUid(userDetails.getUsername());
		
		return MemberResponseDto.from(updated);
	}
	
	@Transactional
	public MemberResponseDto updateMember(MemberModel member) {
		
		memberMapper.updateMember(member);
		
		MemberModel updated = memberMapper.findByUid(member.getUid());
		
		return MemberResponseDto.from(updated);
	}
	
	@Transactional
	public MemberResponseDto changeOwner(MemberModel member, CustomUserDetails userDetails) {
		
		member.setRank("owner");
		memberMapper.updateMember(member);
		
		MemberModel req = userDetails.getMember();
		req.setRank("manager");
		memberMapper.updateMember(req);
		
		MemberModel updated = memberMapper.findByUid(userDetails.getMember().getUid());
		
		return MemberResponseDto.from(updated);
	}
	
	public MembersResponseDto getAllMember(MemberModel model) {
		List<MemberModel> members = memberMapper.getAllMember(model);
		return new MembersResponseDto(members);
	}
	
	@Transactional
	public void deactivate(CustomUserDetails userDetails) {

		String uid = userDetails.getUsername();
		String boxCode = userDetails.getMember().getBoxCode();

		if(!boxCode.isEmpty()) {
			//참여 채널에서 나가기
			List<ChannelModel> models = channelMapper.selectChannelsByUid(boxCode, uid);
			for(ChannelModel model : models) {
				channelService.deleteParticipant(boxCode, model.getChannelId(), uid);
			}

			//멤버십 삭제하기
			MembershipModel membership = membershipService.getMembership(boxCode, uid);
			if(membership != null) {
				membershipService.deleteMembership(membership, uid);
			}
		}

		//FCM 토큰 삭제하기
		fcMtokenMapper.deleteToken(uid);

		//refresh 토큰 삭제하기
		commonMapper.deleteRefreshToken(uid);

		int updatedRows = memberMapper.deleteMember(uid);
		if(updatedRows == 0) {
			throw new IllegalStateException("탈퇴 처리에 실패했습니다.");
		}
	}
	
	//매니저 또는 오너 에게 새멤버 알림
	private void sendNewMemberPush(MemberModel model) {
        // boxCode에 해당하는 관리자·소유자 userId 리스트만 조회
		model.setRank("managerOrOwner");
        List<MemberModel> managers = memberMapper.getAllMember(model);
        if (managers.isEmpty()) return;

        PushModel push = new PushModel();
		push.setTitle("New member!");
		push.setBody(model.getName()+"님이 가입신청했어요.");

        for (MemberModel manager : managers) {
            try {
                pushService.notifyUser(manager, push);
                log.info("푸시 발송 성공 → userId={}", manager);
            } catch (Exception ex) {
                log.warn("푸시 발송 실패 → userId={}, error={}", manager, ex.toString());
            }
        }
    }

}
