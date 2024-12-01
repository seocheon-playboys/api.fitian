package com.seocheon.fitian.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.seocheon.fitian.model.WodModel;

@Mapper
public interface WodMapper {

	WodModel getWod(WodModel wod);
	
	void createWod(WodModel wod);
	
	void updateWod(WodModel wod);
	
	void deleteWod(WodModel wod);
}
