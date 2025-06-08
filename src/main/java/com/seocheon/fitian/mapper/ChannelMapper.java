package com.seocheon.fitian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seocheon.fitian.model.ChannelModel;

@Mapper
public interface ChannelMapper {

	void insertChannel(ChannelModel model);
	
	ChannelModel selectChannelById(@Param("boxCode") String boxCode, @Param("channelId") String channelId);
	
	List<ChannelModel> selectChannelsByBoxCode(@Param("boxCode") String boxCode);
}
