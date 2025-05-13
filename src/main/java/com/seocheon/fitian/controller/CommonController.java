package com.seocheon.fitian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.seocheon.fitian.model.CommonModel;
import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.service.CommonService;

@RestController
@CrossOrigin(origins = "http://localhost:8088")
public class CommonController {

	@Autowired
	private CommonService sv;
	
	@GetMapping("/check")
	public String home() {
		return "Server is now working";
	}
	
	@GetMapping("/common/getPolicy")
    public ResponseModel getMember(@RequestBody CommonModel model) {
    	ResponseModel res = sv.getPolicy(model);
    	return res;
    }
}
