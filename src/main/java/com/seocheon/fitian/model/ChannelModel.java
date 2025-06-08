package com.seocheon.fitian.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelModel {
	private int channelNo;
	private String boxCode;
	private String channelId;
	private String channelName;
	private String type;
	private String createdBy;
	private LocalDateTime createdAt;
}
