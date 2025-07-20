package com.seocheon.fitian.dto.classes;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SearchClassDTO {
	private String boxCode;
	private LocalDate startDate;
	private LocalDate endDate;
}
