package com.seocheon.fitian.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seocheon.fitian.mapper.CommonMapper;
import com.seocheon.fitian.model.CommonModel;
import com.seocheon.fitian.model.ResponseModel;

@Service
public class CommonService {

	private final CommonMapper mapper;
	
	@Autowired
	public CommonService(CommonMapper mapper) {
		this.mapper = mapper;
	}
	
	public ResponseModel getPolicy(CommonModel model) {
		ResponseModel res = new ResponseModel();
		List<CommonModel> contents = mapper.getPolicy(model);
		
		res.setCommonModelList(contents);
		
		return res;
	}
}
