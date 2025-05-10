package com.seocheon.fitian.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "와드 정보 DTO")
public class WodModel {

	private int wodNo;
	private String wodDate;
	private String wodScript;
	private String wodType;
	private String wodTitle;
	private String boxCode;
}
