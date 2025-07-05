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
	
	List<ChannelModel> selectChannelsByUid(@Param("boxCode") String boxCode, @Param("uid") String uid);
	
	List<ChannelModel> selectChannelsByType(@Param("boxCode") String boxCode, @Param("type") String type);
	
	void deleteChannel(@Param("boxCode") String boxCode, @Param("channelId") String channelId);
}
