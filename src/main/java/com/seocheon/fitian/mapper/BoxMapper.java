package com.seocheon.fitian.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.seocheon.fitian.model.BoxModel;

@Mapper
public interface BoxMapper {

	BoxModel getBox(BoxModel Box);
	
	void createBox(BoxModel Box);
	
	void updateBox(BoxModel Box);
	
	void deleteBox(BoxModel Box);
}
