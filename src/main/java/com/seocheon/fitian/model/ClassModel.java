package com.seocheon.fitian.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassModel {

	private int classNo;
	private String boxCode;
	private LocalDate classDate;
	private String startTime;
	private String endTime;
	private int classMemberCapacity;
	private String classTitle;
	private String classMemo;
	private int participantCount;
}
