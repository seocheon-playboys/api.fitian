package com.seocheon.fitian.dto;

import java.util.List;

import com.seocheon.fitian.model.BoxModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoxesResponseDto {
	private List<BoxSummaryDto> boxes;
}
