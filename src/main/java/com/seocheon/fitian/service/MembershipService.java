package com.seocheon.fitian.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seocheon.fitian.mapper.MembershipHistoryMapper;
import com.seocheon.fitian.mapper.MembershipMapper;
import com.seocheon.fitian.model.MembershipHistoryModel;
import com.seocheon.fitian.model.MembershipModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor 
public class MembershipService {
	
	private final MembershipMapper membershipMapper;
	private final MembershipHistoryMapper membershipHistoryMapper;
    private final ObjectMapper objectMapper;
	
	public MembershipModel getMembership(String boxCode, String uid) {
		return membershipMapper.findByUid(boxCode, uid);
	}
	
	@Transactional
	public void createMembership(MembershipModel model, String performedBy) {
		
		membershipMapper.createMembership(model);
		try {
			MembershipModel now = membershipMapper.findByUid(model.getBoxCode(), model.getUid());
            String newValue = objectMapper.writeValueAsString(model);
            
            MembershipHistoryModel history = MembershipHistoryModel.builder()
                    .membershipNo(now.getMembershipNo())
                    .actionType("CREATE")
                    .prevValue(null)
                    .newValue(newValue)
                    .performedBy(performedBy)
                    .memo(null) // 필요시 입력
                    .period(model.getPeriod())
                    .build();
            membershipHistoryMapper.createHistory(history);
		}catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	public void updateMembership(MembershipModel model, String performedBy) {
		MembershipModel prev = membershipMapper.findByUid(model.getBoxCode(), model.getUid());
		membershipMapper.updateMembership(model);
		try {
			String prevValue = objectMapper.writeValueAsString(prev);
            String newValue = objectMapper.writeValueAsString(model);
            
            MembershipHistoryModel history = MembershipHistoryModel.builder()
                    .membershipNo(model.getMembershipNo())
                    .actionType("UPDATE")
                    .prevValue(prevValue)
                    .newValue(newValue)
                    .performedBy(performedBy)
                    .memo(model.getMemo()) // 필요시 입력
                    .period(model.getPeriod())
                    .build();
            membershipHistoryMapper.createHistory(history);
		}catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	public void deleteMembership(MembershipModel model, String performedBy) {
		MembershipModel prev = membershipMapper.findByUid(model.getBoxCode(), model.getUid());
		
		membershipMapper.deleteMembership(model);
		
		try {
			String prevValue = objectMapper.writeValueAsString(prev);
            
            MembershipHistoryModel history = MembershipHistoryModel.builder()
                    .membershipNo(model.getMembershipNo())
                    .actionType("DELETE")
                    .prevValue(prevValue)
                    .newValue(null)
                    .performedBy(performedBy)
                    .memo(null) // 필요시 입력
                    .period(0)
                    .build();
            membershipHistoryMapper.createHistory(history);
		}catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	public List<MembershipHistoryModel> viewMembershipHistory(int membershipNo) {
		return membershipHistoryMapper.findByUid(membershipNo);
	}

}
