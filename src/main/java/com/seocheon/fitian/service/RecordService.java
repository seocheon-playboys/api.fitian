package com.seocheon.fitian.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seocheon.fitian.mapper.RecordMapper;
import com.seocheon.fitian.mapper.WodMapper;
import com.seocheon.fitian.model.RecordModel;
import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.model.WodModel;

@Service
public class RecordService {
	private final RecordMapper mapper;
	private final WodMapper wodMapper;
	@Autowired
	public RecordService(RecordMapper mapper, WodMapper wodMapper) {
		this.mapper = mapper;
		this.wodMapper = wodMapper;
	}
	
	public ResponseModel getRecord(WodModel model) {
		
		ResponseModel res = new ResponseModel();
		RecordModel basic = new RecordModel();
		
		WodModel wod = wodMapper.getWod(model);
		
		basic.setWodNo(wod.getWodNo());
		
		List<RecordModel> recordList = mapper.getRecord(basic);
		for(int i = 0; i<recordList.size(); i++) {
			String checkDot = recordList.get(i).getRound();
			if(checkDot.contains(".")) {
				checkDot = checkDot.replace(".", "R ");
				recordList.get(i).setRound(checkDot);
			} else {
				recordList.get(i).setRound(checkDot+"R");
			}
		}
		
		res.setRecordModelList(recordList);
		
		return res;
	}
	
	public ResponseModel createRecord(RecordModel model) {
		ResponseModel res = new ResponseModel();
		
		mapper.createRecord(model);
		
		res.setMessage("success");
		
		return res;
	}
	
	public ResponseModel updateRecord(RecordModel model) {
		ResponseModel res = new ResponseModel();
		
		mapper.updateRecord(model);
		
		res.setMessage("success");
		
		return res;
	}
	
	public ResponseModel deleteRecord(RecordModel model) {
		ResponseModel res = new ResponseModel();
		
		mapper.deleteRecord(model);
		
		res.setMessage("success");
		
		return res;
	}
}
