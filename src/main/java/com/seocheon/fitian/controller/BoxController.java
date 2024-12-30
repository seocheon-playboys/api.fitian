package com.seocheon.fitian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seocheon.fitian.model.BoxModel;
import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.service.BoxService;

@RestController
public class BoxController {

	@Autowired
	private BoxService sv;
	
	@RequestMapping("/box/getBox")
    public ResponseModel getBox(@RequestBody BoxModel model) {
    	ResponseModel res = sv.getBox(model);
    	return res;
    }
	
	@RequestMapping("/box/createBox")
    public ResponseModel createBox(@RequestBody BoxModel model) {
    	ResponseModel res = sv.createBox(model);
    	return res;
    }
	
	@RequestMapping("/box/updateBox")
    public ResponseModel updateBox(@RequestBody BoxModel model) {
    	ResponseModel res = sv.updateBox(model);
    	return res;
    }
	
	/*
	@RequestMapping("/Box/deleteBox")
    public ResponseModel deleteBox(@RequestBody BoxModel model) {
    	ResponseModel res = sv.deleteBox(model);
    	return res;
    }
    */
}
