package com.seocheon.fitian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.model.WodModel;
import com.seocheon.fitian.service.WodService;

@RestController
public class WodController {

	@Autowired
	private WodService sv;
	
	@RequestMapping("/wod/getWod")
    public ResponseModel getWod(@RequestBody WodModel model) {
    	ResponseModel res = sv.getWod(model);
    	return res;
    }
	
	@RequestMapping("/wod/createWod")
    public ResponseModel createWod(@RequestBody WodModel model) {
    	ResponseModel res = sv.createWod(model);
    	return res;
    }
	
	@RequestMapping("/wod/updateWod")
    public ResponseModel updateWod(@RequestBody WodModel model) {
    	ResponseModel res = sv.updateWod(model);
    	return res;
    }
	
	@RequestMapping("/wod/deleteWod")
    public ResponseModel deleteWod(@RequestBody WodModel model) {
    	ResponseModel res = sv.deleteWod(model);
    	return res;
    }
}
