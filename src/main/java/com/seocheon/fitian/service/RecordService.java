package com.seocheon.fitian.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seocheon.fitian.dto.RecordResponseDto;
import com.seocheon.fitian.mapper.RecordMapper;
import com.seocheon.fitian.mapper.WodMapper;
import com.seocheon.fitian.model.RecordModel;
import com.seocheon.fitian.model.WodModel;

@Service
public class RecordService {
	
	private final RecordMapper mapper;
	
	@Autowired
	public RecordService(RecordMapper mapper, WodMapper wodMapper) {
		this.mapper = mapper;
	}
	
	public RecordModel getRecord(int recordNo) {
		return mapper.getRecord(recordNo);
	}
	
	public List<RecordResponseDto> getRecordList(WodModel wod) {
		
		String wodType = wod.getWodType(); //와드타입 확인
		
		List<RecordModel> recordModelList = mapper.getRecordList(wod); //basic에 있는 와드 넘버로 레코드를 가져옴
		
		if(wodType.equals("ForTime")) {
			
			for(int i = 0; i<recordModelList.size(); i++) {
				String checkDot = recordModelList.get(i).getTime(); //  ":" 있는지 확인
				checkDot = checkDot.replace(".", ":");
				recordModelList.get(i).setTime(checkDot);
			}		
		}
		
		List<RecordResponseDto> recordList = recordModelList.stream()
				.map(RecordResponseDto::from)
				.collect(Collectors.toList());
		
		return recordList;
	}
	
	@Transactional
	public void createRecord(RecordModel model) {
		
		/*
		1) ForTime - mm.ss  시간순
		2) AMRAP - round  라운드/렙스 순
		3) EMOM - s/f  success fail 순
		4) SuccessFail
		5) EMOMMAX - reps
		6) MaxReps
		*/
		
		List<RecordModel> recordList = model.getRecordModelList();

		for(int i = 0; i < recordList.size(); i++) {
			mapper.createRecord(recordList.get(i));
		}
		
	}
	
	@Transactional
	public RecordModel updateRecord(RecordModel model) {
		
		int updatedRows = mapper.updateRecord(model);
		
		if(updatedRows == 0) {
			throw new IllegalStateException("레코드 수정에 실패했습니다.");
		}

		return mapper.getRecord(model.getRecordNo());
	}
	
	@Transactional
	public void deleteRecord(int recordNo) {
		mapper.deleteRecord(recordNo);
	}
	
	@Transactional
	public void deleteAllRecord(int wodNo) {
		mapper.deleteAllRecord(wodNo);	
	}
}
