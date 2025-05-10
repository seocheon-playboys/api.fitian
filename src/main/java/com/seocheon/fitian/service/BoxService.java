package com.seocheon.fitian.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seocheon.fitian.dto.BoxSummaryDto;
import com.seocheon.fitian.dto.UpdateBoxRequest;
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
	
	public BoxModel getBoxByCode(String boxCode) {
		
		return mapper.getBoxByCode(boxCode);
	}
	
	public List<BoxSummaryDto> getBoxList() {
		
		return mapper.getBoxList();
	}
	
	@Transactional
	public BoxModel createBox(BoxModel model) {
		
		mapper.createBox(model);
		BoxModel box = mapper.getBoxByCode(model.getBoxCode());
		
		return box;
	}
	
	@Transactional
	public BoxModel updateBox(UpdateBoxRequest request, String boxCode) {
		
		BoxModel box = mapper.getBoxByCode(boxCode);
		
		if (request.getBoxContact() != null && !request.getBoxContact().isBlank()) {
	        box.setBoxContact(request.getBoxContact());
	    }
		if (request.getBoxAddress() != null && !request.getBoxAddress().isBlank()) {
	        box.setBoxAddress(request.getBoxAddress());
	    }
		if (request.getBoxScript() != null && !request.getBoxScript().isBlank()) {
	        box.setBoxScript(request.getBoxScript());
	    }
		if (request.getBoxInsta() != null && !request.getBoxInsta().isBlank()) {
	        box.setBoxInsta(request.getBoxInsta());
	    }
		if (request.getBoxFeeUrl() != null && !request.getBoxFeeUrl().isBlank()) {
	        box.setBoxFeeUrl(request.getBoxFeeUrl());
	    }
		if (request.getBoxTimeTableUrl() != null && !request.getBoxTimeTableUrl().isBlank()) {
	        box.setBoxTimeTableUrl(request.getBoxTimeTableUrl());
	    }
		
		int updatedRows = mapper.updateBox(box);
		
		if(updatedRows == 0) {
			throw new IllegalStateException("업데이트에 실패했습니다.");
		}
		
		return box;
	}
	
	
	public ResponseModel getBoxImages(BoxModel model) {
		ResponseModel res = new ResponseModel();
		
		BoxModel box = mapper.getBoxImages(model);
		
		res.setBoxModel(box);
		
		return res;
	}
	
	@Transactional
	public ResponseModel deleteBox(BoxModel model) {
		ResponseModel res = new ResponseModel();
		
		mapper.deleteBox(model);
		
		res.setMessage("박스를 삭제했습니다.");
		
		return res;
	}
}
