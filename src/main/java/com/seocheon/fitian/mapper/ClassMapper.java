package com.seocheon.fitian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seocheon.fitian.dto.classes.SearchClassDTO;
import com.seocheon.fitian.model.ClassModel;
import com.seocheon.fitian.model.ClassParticipantModel;

@Mapper
public interface ClassMapper {
	
	List<ClassModel> findByClassDates(SearchClassDTO dto);
	
	ClassModel findByUid(@Param("uid") String uid);
	
	void createClass(ClassModel model);
	
	ClassModel findByClassNo(@Param("classNo") int classNo);
	
	void joinClass(ClassParticipantModel model);
	
	List<ClassParticipantModel> getMembersByClassNo(int classNo);
	
	void updateClass(ClassModel model);
	
	void deleteClass(ClassModel model);
	
}
