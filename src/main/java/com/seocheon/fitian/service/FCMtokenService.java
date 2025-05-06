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
	
	public ResponseModel getToken(FCMtokenModel model) {
		ResponseModel res = new ResponseModel();
		res.setFCMtokenModel(mapper.getToken(model));
		res.setMessage("토큰저장");
		return res;
	}
	
	@Transactional
	public ResponseModel createToken(FCMtokenModel model) {
		ResponseModel res = new ResponseModel();
		mapper.createToken(model);
		return res;
	}
	
	@Transactional
	public ResponseModel deleteToken() {
		ResponseModel res = new ResponseModel();
		
		return res;
	}
}
