package com.seocheon.fitian.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seocheon.fitian.mapper.WodMapper;
import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.model.WodModel;

@Service
public class WodService {

	private final WodMapper mapper;
	
	@Autowired
	public WodService(WodMapper mapper) {
		this.mapper = mapper;
	}
	
	public ResponseModel getWod(WodModel model) {
		ResponseModel res = new ResponseModel();
		WodModel member = mapper.getWod(model);
		
		res.setWodModel(member);
		
		return res;
	}
	
	public ResponseModel createWod(WodModel model) {
		ResponseModel res = new ResponseModel();
		
		mapper.createWod(model);
		
		res.setMessage("success");
		
		return res;
	}
	
	public ResponseModel updateWod(WodModel model) {
		ResponseModel res = new ResponseModel();
		
		mapper.updateWod(model);
		
		res.setMessage("success");
		
		return res;
	}
	
	public ResponseModel deleteWod(WodModel model) {
		ResponseModel res = new ResponseModel();
		
		mapper.deleteWod(model);
		
		res.setMessage("success");
		
		return res;
	}
}
