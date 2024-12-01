package com.seocheon.fitian.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seocheon.fitian.mapper.MemberMapper;
import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.model.MemberModel;

@Service
public class MemberService {
	
	private final MemberMapper mapper;
	
	@Autowired
	public MemberService(MemberMapper mapper) {
		this.mapper = mapper;
	}
	
	public ResponseModel getMember(MemberModel model) {
		ResponseModel res = new ResponseModel();
		MemberModel member = mapper.getMember(model);
		
		res.setMemberModel(member);
		
		return res;
	}
	
	public ResponseModel joinMember(MemberModel model) {
		ResponseModel res = new ResponseModel();
		MemberModel member = mapper.joinMember(model);
		
		res.setMemberModel(member);
		
		return res;
	}
	
	public ResponseModel updateMember(MemberModel model) {
		ResponseModel res = new ResponseModel();
		MemberModel member = mapper.updateMember(model);
		
		res.setMemberModel(member);
		
		return res;
	}
	
	public ResponseModel deleteMember(MemberModel model) {
		ResponseModel res = new ResponseModel();
		MemberModel member = mapper.deleteMember(model);
		
		res.setMemberModel(member);
		
		return res;
	}
	
	public String getTest(String test) {
		
		String answer = mapper.getTest(test);
		
		return answer;
	}

}
