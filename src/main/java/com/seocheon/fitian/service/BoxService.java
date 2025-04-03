package com.seocheon.fitian.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seocheon.fitian.mapper.BoxMapper;
import com.seocheon.fitian.model.BoxModel;
import com.seocheon.fitian.model.ResponseModel;

@Service
public class BoxService {

	private final BoxMapper mapper;
	
	@Autowired
	public BoxService(BoxMapper mapper) {
		this.mapper = mapper;
	}
	
	public ResponseModel getBox(BoxModel model) {
		ResponseModel res = new ResponseModel();
		
		BoxModel box = mapper.getBox(model);
		
		res.setBoxModel(box);
		
		return res;
	}
	
	public ResponseModel getBoxList(BoxModel model) {
		ResponseModel res = new ResponseModel();
		
		List<BoxModel> boxList = mapper.getBoxList();
		
		res.setBoxModelList(boxList);
		
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
	
	public ResponseModel searchBoxCode(BoxModel model) {
		ResponseModel res = new ResponseModel();
		
		mapper.searchBoxCode(model);
		
		int result = mapper.searchBoxCode(model);
		
		System.out.println(result);
		
		if(result==1) {
			res.setMessage("이미 존재하는 박스코드입니다.");
		} else {
			res.setMessage("사용 가능한 박스코드입니다.");
		}
		
		return res;
	}
	
	public ResponseModel getAllBoxCode() {
		ResponseModel res = new ResponseModel();
		
		List<BoxModel> boxList = mapper.getAllBoxCode();
		
		String[] BoxCodeArr = new String[boxList.size()];
		
		for(int i=0; i<boxList.size(); i++) {
			BoxCodeArr[i] = boxList.get(i).getBoxCode();
		}
		
		res.setBoxCodeList(BoxCodeArr);
		
		return res;
	}
	
	public ResponseModel getBoxImages(BoxModel model) {
		ResponseModel res = new ResponseModel();
		
		BoxModel box = mapper.getBoxImages(model);
		
		res.setBoxModel(box);
		
		return res;
	}
	
	public ResponseModel deleteBox(BoxModel model) {
		ResponseModel res = new ResponseModel();
		
		mapper.deleteBox(model);
		
		res.setMessage("success");
		
		return res;
	}
}
