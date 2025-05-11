package com.seocheon.fitian.dto;

import com.seocheon.fitian.model.RecordModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RecordResponseDto {

	@Schema(description = "레코드 넘버")
	private int recordNo;
	
	@Schema(description = "회원 이름")
	private String name;
	
	@Schema(description = "회원 성별")
	private String gender;
	
	@Schema(description = "와드 등급")
	private String level;

	private String round;
	private String reps;
	private String time;
	private String result;

	@Schema(description = "팀 와드 여부")
	private String team;

	@Schema(description = "코치 메모")
	private String memo;

	@Schema(description = "클래스 시간")
	private String classTime;
	
	public static RecordResponseDto from(RecordModel m) {
		return new RecordResponseDto(
					m.getRecordNo(),
					m.getName(),
					m.getGender(),
					m.getLevel(),
					m.getRound(),
					m.getReps(),
					m.getTime(),
					m.getResult(),
					m.getTeam(),
					m.getMemo(),
					m.getClassTime()
				);
	}
}
