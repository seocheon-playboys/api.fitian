package com.seocheon.fitian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.seocheon.fitian.model.BoxModel;

@Mapper
public interface BoxMapper {

	BoxModel getBox(BoxModel Box);
	
	void createBox(BoxModel Box);
	
	void updateBox(BoxModel Box);
	
	void deleteBox(BoxModel Box);
	
	int searchBoxCode(BoxModel Box);
	
	List<BoxModel> getBoxList();
	
	List<BoxModel> getAllBoxCode();
	
	BoxModel getBoxImages(BoxModel Box);
}
