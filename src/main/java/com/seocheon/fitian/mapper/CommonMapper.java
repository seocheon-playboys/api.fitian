package com.seocheon.fitian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.seocheon.fitian.model.CommonModel;

@Mapper
public interface CommonMapper {

	List<CommonModel> getPolicy();
}
