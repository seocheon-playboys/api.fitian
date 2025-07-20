package com.seocheon.fitian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seocheon.fitian.dto.classes.ClassRequest;
import com.seocheon.fitian.dto.classes.SearchClassDTO;
import com.seocheon.fitian.model.ClassModel;

@Mapper
public interface ClassMapper {
	
	List<ClassModel> findByClassDates(SearchClassDTO dto);
	
	ClassModel findByUid(@Param("uid") String uid);
	
	void createClass(ClassModel model);
}
