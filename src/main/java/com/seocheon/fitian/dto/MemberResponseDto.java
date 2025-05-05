package com.seocheon.fitian.dto;

import com.seocheon.fitian.model.MemberModel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponseDto {

	private String name;
	private String nickname;
	private String boxName;
	private String email;
	private String rank;
	private String gender;
	private String boxCode;
	private String useYN;
	private String joinDate;
	
	public static MemberResponseDto from(MemberModel m) {
		return new MemberResponseDto(
					m.getName(),
					m.getNickname(),
		            m.getBoxName(),
		            m.getEmail(),
		            m.getRank(),
		            m.getGender(),
		            m.getBoxCode(),
		            m.getUseYN(),
		            m.getJoinDate()
				);
	}
}
