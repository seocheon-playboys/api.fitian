package com.seocheon.fitian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seocheon.fitian.model.MemberModel;
import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.service.MemberService;

@RestController
public class MemberController {

	@Autowired
	private MemberService sv;
	
	@RequestMapping("/member/getMember")
    public ResponseModel getMember(@RequestBody MemberModel model) {
    	ResponseModel res = sv.getMember(model);
    	return res;
    }
	
	@RequestMapping("/member/joinMember")
    public ResponseModel joinMember(@RequestBody MemberModel model) {
    	ResponseModel res = sv.joinMember(model);
    	return res;
    }
	
	@RequestMapping("/member/updateMember")
    public ResponseModel updateMember(@RequestBody MemberModel model) {
    	ResponseModel res = sv.updateMember(model);
    	return res;
    }
	
	@RequestMapping("/member/deleteMember")
    public ResponseModel deleteMember(@RequestBody MemberModel model) {
    	ResponseModel res = sv.deleteMember(model);
    	return res;
    }
	
	@RequestMapping("/member/getTest")
    public String getTest() {
		/*
		 * System.out.println(model.getName()); model.setName("테스트"); String res =
		 * sv.getTest(model.getName());
		 */
		String test = "Hello world!";
    	System.out.println("test");
    	return test;
    }

}
