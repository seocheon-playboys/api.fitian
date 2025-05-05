package com.seocheon.fitian.dto;

import java.util.List;

import com.seocheon.fitian.model.MemberModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MembersResponseDto {
	private List<MemberModel> members;
}
