package com.seocheon.fitian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seocheon.fitian.model.FCMtokenModel;
import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.service.FCMtokenService;

@RestController
@CrossOrigin(origins = "http://localhost:8088")
public class FCMtokenController {

	@Autowired
	private FCMtokenService sv;
	
	@RequestMapping("/token/getToken")
    public ResponseModel getToken(@RequestBody FCMtokenModel model) {
    	ResponseModel res = sv.getToken(model);
    	return res;
    }
	
	@RequestMapping("/token/createToken")
    public ResponseModel createToken(@RequestBody FCMtokenModel model) {
    	ResponseModel res = sv.createToken(model);
    	return res;
    }
	
}
