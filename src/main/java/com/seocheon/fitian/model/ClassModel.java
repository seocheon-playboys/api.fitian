package com.seocheon.fitian.model;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClassModel {

	private int classNo;
	private String boxCode;
	private LocalDate classDate;
	private String startTime;
	private String endTime;
	private int classMemberCapacity;
	private String classTitle;
	private String classMemo;
}
