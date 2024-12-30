package com.seocheon.fitian.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.seocheon.fitian.mapper.BoxMapper;
import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.model.BoxModel;

public class BoxService {

private final BoxMapper mapper;
	
	@Autowired
	public BoxService(BoxMapper mapper) {
		this.mapper = mapper;
	}
	
	public ResponseModel getBox(BoxModel model) {
		ResponseModel res = new ResponseModel();
		BoxModel member = mapper.getBox(model);
		
		res.setBoxModel(member);
		
		return res;
	}
	
	public ResponseModel createBox(BoxModel model) {
		ResponseModel res = new ResponseModel();
		
		mapper.createBox(model);
		
		res.setMessage("success");
		
		return res;
	}
	
	public ResponseModel updateBox(BoxModel model) {
		ResponseModel res = new ResponseModel();
		
		mapper.updateBox(model);
		
		res.setMessage("success");
		
		return res;
	}
	
	public ResponseModel deleteBox(BoxModel model) {
		ResponseModel res = new ResponseModel();
		
		mapper.deleteBox(model);
		
		res.setMessage("success");
		
		return res;
	}
}
