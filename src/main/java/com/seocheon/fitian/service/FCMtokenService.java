package com.seocheon.fitian.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public ResponseModel createToken(FCMtokenModel model) {
		ResponseModel res = new ResponseModel();
		mapper.createToken(model);
		return res;
	}
	
	public ResponseModel deleteToken() {
		ResponseModel res = new ResponseModel();
		
		return res;
	}
}
