package com.seocheon.fitian.dto;

import com.seocheon.fitian.model.BoxModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoxSummaryDto {

	@Schema(description = "박스 코드")
	private String boxCode;
	
	@Schema(description = "박스 이름")
	private String boxName;
	
	@Schema(description = "박스 주소")
	private String boxAddress;
	
	public static BoxSummaryDto from(BoxModel m) {
		return new BoxSummaryDto(
					m.getBoxCode(),
					m.getBoxName(),
					m.getBoxAddress()
				);
	}
}
