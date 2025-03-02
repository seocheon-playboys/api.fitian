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
		
		String wodType = wod.getWodType(); //와드타입 확인
		
		basic.setWodNo(wod.getWodNo()); //얻은 와드 넘버를 레코드모델 basic에 넣음.
		
		List<RecordModel> recordList = mapper.getRecord(basic); //basic에 있는 와드 넘버로 레코드를 가져옴
		
		if(wodType.equals("ForTime")) {
			
			for(int i = 0; i<recordList.size(); i++) {
				String checkDot = recordList.get(i).getTime(); //  ":" 있는지 확인
				checkDot = checkDot.replace(".", ":");
				recordList.get(i).setTime(checkDot);
			}
			res.setRecordModelList(recordList);
			
		} else {
			
			res.setRecordModelList(recordList);
			
		}
		return res;
	}
	
	public ResponseModel createRecord(RecordModel model) {
		
		/*
		1) ForTime - mm.ss  시간순
		2) AMRAP - round  라운드/렙스 순
		3) EMOM - s/f  success fail 순
		4) SuccessFail
		5) EMOMMAX - reps
		6) MaxReps
		*/

		ResponseModel res = new ResponseModel();
		
		List<RecordModel> recordList = model.getRecordModelList();

		for(int i = 0; i < recordList.size(); i++) {
			mapper.createRecord(recordList.get(i));
		}
		
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
