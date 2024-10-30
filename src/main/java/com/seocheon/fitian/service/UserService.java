package com.seocheon.fitian.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seocheon.fitian.mapper.UserMapper;
import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.model.MemberModel;

@Service
public class UserService {
	
	private final UserMapper mapper;
	
	@Autowired
	public UserService(UserMapper mapper) {
		this.mapper = mapper;
	}
	
	public ResponseModel join(MemberModel model) {
		
		
		return null;
		
	}

}
