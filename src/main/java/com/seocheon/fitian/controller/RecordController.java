package com.seocheon.fitian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seocheon.fitian.model.RecordModel;
import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.model.WodModel;
import com.seocheon.fitian.service.RecordService;

@RestController
@CrossOrigin(origins = "http://localhost:8088")
public class RecordController {

	@Autowired
	private RecordService sv;
	
	@RequestMapping("/record/getRecord")
    public ResponseModel getRecord(@RequestBody WodModel model) {
    	ResponseModel res = sv.getRecord(model);
    	return res;
    }
	
	@RequestMapping("/record/createRecord")
    public ResponseModel createRecord(@RequestBody RecordModel model) {
    	ResponseModel res = sv.createRecord(model);
    	return res;
    }
	
	@RequestMapping("/record/updateRecord")
    public ResponseModel updateRecord(@RequestBody RecordModel model) {
    	ResponseModel res = sv.updateRecord(model);
    	return res;
    }
	
	@RequestMapping("/record/deleteRecord")
    public ResponseModel deleteRecord(@RequestBody RecordModel model) {
    	ResponseModel res = sv.deleteRecord(model);
    	return res;
    }
	
	@RequestMapping("/record/deleteAllRecord")
    public ResponseModel deleteAllRecord(@RequestBody RecordModel model) {
    	ResponseModel res = sv.deleteAllRecord(model);
    	return res;
    }
	
}
