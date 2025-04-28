package com.seocheon.fitian.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
	
	private final MemberMapper mapper;
	private final PushService pushService;
	
	public ResponseModel getMember(MemberModel model) {
		ResponseModel res = new ResponseModel();
		MemberModel member = mapper.getMember(model);
		
		res.setMemberModel(member);
		
		return res;
	}
	
	public ResponseModel getAllMember(MemberModel model) {
		ResponseModel res = new ResponseModel();
		List<MemberModel> members = mapper.getAllMember(model);
		
		res.setMemberModelList(members);
		
		return res;
	}
	
	@Transactional
	public ResponseModel joinMember(MemberModel model) {
		ResponseModel res = new ResponseModel();
		
		if (mapper.existMember(model) == 1) {
            res.setMessage("이미 존재하는 회원입니다.");
            return res;
        }
		
		// 현재 시간 → 서울 기준 ISO_DATE 형식으로
		String joinTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		model.setJoinDate(joinTime); // 모델에 시간 설정
		
		//mapper.joinMember(model);
        res.setMessage("회원가입 완료");
		
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                sendNewMemberPush(model);
            }
        });

        return res;
	}
	
	public ResponseModel updateMember(MemberModel model) {
		ResponseModel res = new ResponseModel();
		
		try {
			mapper.updateMember(model);
			res.setMessage("회원 정보 변경 완료");
		} catch(Exception e) {
			res.setMessage("회원 정보 변경 실패");
		}	
		return res;
	}
	
	public ResponseModel deleteMember(MemberModel model) {
		ResponseModel res = new ResponseModel();
		
		try {
			mapper.deleteMember(model);
			res.setMessage("회원 탈퇴 완료");
		} catch(Exception e) {
			res.setMessage("회원 탈퇴 실패");
		}	
		return res;
	}
	
	public ResponseModel existMember(MemberModel model) {
		ResponseModel res = new ResponseModel();
		
		int result = mapper.existMember(model);
		
		if(result==1) {
			MemberModel existMember = mapper.getMember(model);
			res.setMessage("true");
			res.setMemberModel(existMember);
		} else {
			res.setMessage("false");
		}
		
		return res;
	}
	
	//매니저 또는 오너 에게 새멤버 알림
	private void sendNewMemberPush(MemberModel model) {
        // boxCode에 해당하는 관리자·소유자 userId 리스트만 조회
		model.setRank("managerOrOwner");
        List<MemberModel> managers = mapper.getAllMember(model);
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
