package com.seocheon.fitian.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seocheon.fitian.mapper.FCMtokenMapper;
import com.seocheon.fitian.model.FCMtokenModel;
import com.seocheon.fitian.model.ResponseModel;

@Service
public class FCMtokenService {

	private final FCMtokenMapper mapper;
	
	@Autowired
	public FCMtokenService(FCMtokenMapper mapper) {
		this.mapper = mapper;
	}
	
	public FCMtokenModel getToken(FCMtokenModel model) {
		return mapper.getToken(model);
	}
	
	@Transactional
	public void createToken(FCMtokenModel model) {
		mapper.createToken(model);
	}
	
	@Transactional
	public ResponseModel deleteToken() {
		ResponseModel res = new ResponseModel();
		
		return res;
	}
}
