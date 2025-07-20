package com.seocheon.fitian.dto.classes;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassRequest {
	
	private String boxCode;
	private LocalDate classDate;
	private String startTime;
	private String endTime;
	private int classMemberCapacity;
	private String classTitle;
	private String classMemo;
}
