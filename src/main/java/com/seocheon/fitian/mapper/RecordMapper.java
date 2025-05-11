package com.seocheon.fitian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seocheon.fitian.model.RecordModel;
import com.seocheon.fitian.model.WodModel;

@Mapper
public interface RecordMapper {
	
	List<RecordModel> getRecordList(WodModel wod);
	
	RecordModel getRecord(@Param("recordNo") int recordNo);
	
	void createRecord(RecordModel Record);
	
	int updateRecord(RecordModel Record);
	
	void deleteRecord(@Param("recordNo") int recordNo);
	
	void deleteAllRecord(@Param("wodNo") int wodNo);
}
