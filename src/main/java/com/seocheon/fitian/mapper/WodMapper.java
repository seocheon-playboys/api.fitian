package com.seocheon.fitian.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seocheon.fitian.model.WodModel;

@Mapper
public interface WodMapper {

	WodModel getWod(WodModel wod);
	
	WodModel getWodByNo(@Param("wodNo") int wodNo);
	
	void createWod(WodModel wod);
	
	void updateWod(WodModel wod);
	
	void deleteWod(WodModel wod);
}
