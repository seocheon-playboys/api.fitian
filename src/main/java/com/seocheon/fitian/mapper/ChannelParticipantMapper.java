package com.seocheon.fitian.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seocheon.fitian.dto.channel.ChannelParticipantResponseDto;
import com.seocheon.fitian.model.ChannelParticipantModel;

@Mapper
public interface ChannelParticipantMapper {
	
	void insertParticipant(ChannelParticipantModel model);
	
	List<ChannelParticipantResponseDto> selectUidsByBoxAndChannel(@Param("boxCode") String boxCode, @Param("channelId") String channelId);
	
	List<String> selectExistingUids(@Param("boxCode") String boxCode, @Param("channelId") String channelId);

	void deleteParticipant(@Param("boxCode") String boxCode, @Param("channelId") String channelId, @Param("uid") String uid);
	
	boolean isParticipant(@Param("boxCode") String boxCode, @Param("channelId") String channelId, @Param("uid") String uid);
	
	void deleteAllParticipant(@Param("boxCode") String boxCode, @Param("channelId") String channelId);
}
