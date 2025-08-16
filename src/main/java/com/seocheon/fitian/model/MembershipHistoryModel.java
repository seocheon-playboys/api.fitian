package com.seocheon.fitian.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipHistoryModel {
	private int historyNo;
    private int membershipNo;
    private String actionType;
    private int period;
    private String prevValue;
    private String newValue;
    private String performedBy;
    private LocalDateTime performedAt;
    private String memo;
}
