package com.seocheon.fitian.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
public class MembershipModel {

	private int membershipNo;
	private String uid;
	private String boxCode;
	private String membershipName;
	private LocalDate startDate;
	private LocalDate expirationDate;
	private String status;
	private String memo;
	private LocalDateTime createdAt;
	private int period;
}
