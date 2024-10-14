package com.seocheon.fitian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.model.UserModel;
import com.seocheon.fitian.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService sv;
	
	@RequestMapping("/user/join")
    public ResponseModel join(@RequestBody UserModel model) {
    	ResponseModel res = sv.join(model);
    	return res;
    }
}
