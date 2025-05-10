package com.seocheon.fitian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seocheon.fitian.dto.BoxSummaryDto;
import com.seocheon.fitian.model.BoxModel;

@Mapper
public interface BoxMapper {
	BoxModel getBoxByCode(@Param("boxCode")String boxCode);
	
	void createBox(BoxModel Box);
	
	int updateBox(BoxModel box);
	
	void deleteBox(BoxModel Box);
	
	List<BoxSummaryDto> getBoxList();
	
	BoxModel getBoxImages(BoxModel Box);
}
