package com.seocheon.fitian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seocheon.fitian.model.CommonModel;

@Mapper
public interface CommonMapper {

	List<CommonModel> getPolicy(@Param("title") String title);
	
	void createRefreshToken(@Param("uid") String uid, @Param("token") String token);
	
	String getRefreshToken(@Param("uid") String uid);
	
	void deleteRefreshToken(@Param("uid") String uid);
}
