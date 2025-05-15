package com.seocheon.fitian.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seocheon.fitian.mapper.MemberMapper;
import com.seocheon.fitian.mapper.RecordMapper;
import com.seocheon.fitian.mapper.WodMapper;
import com.seocheon.fitian.model.ResponseModel;
import com.seocheon.fitian.model.WodModel;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class WodService {

	private final WodMapper mapper;
	private final RecordMapper recordMapper;
	
	public WodModel getWod(WodModel model) {

		WodModel wod = mapper.getWod(model);
		
		return wod;
	}
	
	public WodModel getWodByNo(int wodNo) {

		WodModel wod = mapper.getWodByNo(wodNo);
		
		return wod;
	}
	
	@Transactional
	public WodModel createWod(WodModel model) {
		
		WodModel existWod = mapper.getWod(model);
		if(existWod == null) {
			mapper.createWod(model);
		}
		WodModel wod = mapper.getWod(model);
		return wod;
	}
	
	@Transactional
	public WodModel updateWod(WodModel model) {
		
		mapper.updateWod(model);
		
		WodModel wod = mapper.getWod(model);
		
		return wod;
	}
	
	@Transactional
	public ResponseModel deleteWod(WodModel model) {
		ResponseModel res = new ResponseModel();
		
		mapper.deleteWod(model);
		recordMapper.deleteAllRecord(model.getWodNo());
		res.setMessage("와드를 삭제했습니다.");
		
		return res;
	}
}
