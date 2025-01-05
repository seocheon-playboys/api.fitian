package com.seocheon.fitian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.seocheon.fitian.model.RecordModel;

@Mapper
public interface RecordMapper {
	
	List<RecordModel> getRecord(RecordModel Record);
	
	void createRecord(RecordModel Record);
	
	void updateRecord(RecordModel Record);
	
	void deleteRecord(RecordModel Record);
}
