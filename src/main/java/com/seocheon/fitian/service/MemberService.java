package com.seocheon.fitian.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.seocheon.fitian.auth.security.CustomUserDetails;
import com.seocheon.fitian.dto.MemberResponseDto;
import com.seocheon.fitian.dto.MembersResponseDto;
import com.seocheon.fitian.dto.UpdateSelfRequest;
import com.seocheon.fitian.mapper.MemberMapper;
import com.seocheon.fitian.model.MemberModel;
import com.seocheon.fitian.model.PushModel;
import com.seocheon.fitian.model.ResponseModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor 
public class MemberService {
	
	private final MemberMapper memberMapper;
	private final PushService pushService;
	
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
		
		memberMapper.updateMember(member);
		
		MemberModel updated = memberMapper.findByUid(userDetails.getUsername());
		
		return MemberResponseDto.from(updated);
	}
	
	public MemberResponseDto updateMember(MemberModel member) {
		
		memberMapper.updateMember(member);
		
		MemberModel updated = memberMapper.findByUid(member.getUid());
		
		return MemberResponseDto.from(updated);
	}
	
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
	
	public void deactivate(String uid) {
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
