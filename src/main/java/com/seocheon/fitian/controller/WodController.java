package com.seocheon.fitian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.model.WodModel;
import com.seocheon.fitian.service.WodService;

@RestController
@CrossOrigin(origins = "http://localhost:8088")
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
	
	@RequestMapping("/wod/createWodTest1")
    public void createWodTest1() {
		
		WodModel model = new WodModel();
		
		for(int i = 1; i<10; i++) {
			model.setBoxNo(1111);
			model.setWodDate("2025-03-0"+i);
			model.setWodScript("03/"+i+" 와드입니다.");
			model.setWodType("ForTime");
			sv.createWod(model);
			System.out.println("03/0"+i+" done");
		}
    }
	
	@RequestMapping("/wod/createWodTest2")
    public void createWodTest2() {
		
		WodModel model = new WodModel();
		
		for(int i = 10; i<16; i++) {
			model.setBoxNo(1111);
			model.setWodDate("2025-03-"+i);
			model.setWodScript("03/"+i+" 와드입니다.");
			model.setWodType("AMRAP");
			sv.createWod(model);
			System.out.println("03/"+i+" done");
		}
    }
	
}
