package com.seocheon.fitian.dto;

import com.seocheon.fitian.model.BoxModel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoxResponseDto {
	private String boxName;
	private String boxCode;
	private String boxContact;
	private String boxAddress;
	private String boxScript;
	private String boxInsta;
	private String placeId;
	private String boxFeeUrl;
	private String boxTimeTableUrl;
	
	public static BoxResponseDto from (BoxModel m) {
		return new BoxResponseDto(
				m.getBoxName(),
				m.getBoxCode(),
				m.getBoxContact(),
				m.getBoxAddress(),
				m.getBoxScript(),
				m.getBoxInsta(),
				m.getPlaceId(),
				m.getBoxFeeUrl(),
				m.getBoxTimeTableUrl()
			);
	}
}
