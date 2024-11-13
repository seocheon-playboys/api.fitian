package com.seocheon.fitian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seocheon.fitian.model.MemberModel;
import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService sv;
	
	@RequestMapping("/user/getMember")
    public ResponseModel getMember(@RequestBody MemberModel model) {
    	ResponseModel res = sv.getMember(model);
    	return res;
    }
	
	@RequestMapping("/user/getTest")
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
