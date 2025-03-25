package com.seocheon.fitian.service;

import java.util.List;
import java.util.Map;

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
	
	public ResponseModel getAllMember(MemberModel model) {
		ResponseModel res = new ResponseModel();
		List<MemberModel> members = mapper.getAllMember(model);
		
		res.setMemberModelList(members);
		
		return res;
	}
	
	public ResponseModel joinMember(MemberModel model) {
		ResponseModel res = new ResponseModel();
		
		int result = mapper.existMember(model); //uid로 중복 회원 체크
		
		if(result != 1) {
			try {
				mapper.joinMember(model);
				res.setMessage("회원가입 완료");
			} catch(Exception e) {
				res.setMessage("회원가입 실패");
			}
		} else {
			res.setMessage("이미 존재하는 회원입니다.");
		}
		
		return res;
	}
	
	public ResponseModel updateMember(MemberModel model) {
		ResponseModel res = new ResponseModel();
		
		try {
			mapper.updateMember(model);
			res.setMessage("변경 완료");
		} catch(Exception e) {
			res.setMessage("변경 실패");
		}	
		return res;
	}
	
	public ResponseModel deleteMember(MemberModel model) {
		ResponseModel res = new ResponseModel();
		
		try {
			mapper.deleteMember(model);
			res.setMessage("탈퇴 완료");
		} catch(Exception e) {
			res.setMessage("탈퇴 실패");
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
	
	public String getTest(String test) {
		
		String answer = mapper.getTest(test);
		
		return answer;
	}

}
