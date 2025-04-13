package com.seocheon.fitian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.service.CommonService;

@RestController
@CrossOrigin(origins = "http://localhost:8088")
public class CommonController {

	@Autowired
	private CommonService sv;
	
	@RequestMapping("/common/getPolicy")
    public ResponseModel getMember() {
    	ResponseModel res = sv.getPolicy();
    	return res;
    }
}
