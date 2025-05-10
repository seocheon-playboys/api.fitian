package com.seocheon.fitian.auth.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.seocheon.fitian.auth.annotation.AllowedRanks;
import com.seocheon.fitian.auth.security.CustomUserDetails;
import com.seocheon.fitian.model.MemberModel;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class RankAuthorizationAspect {

	@Before("@annotation(allowedRanks)")
	public void checkRank(JoinPoint joinPoint, AllowedRanks allowedRanks) {
		CustomUserDetails userDetails = 
				(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MemberModel member = userDetails.getMember();
		
		String userRank = member.getRank().toUpperCase();
		for(String allowed : allowedRanks.value()) {
			if(allowed.equalsIgnoreCase(userRank)) {
				return;
			}
		}
		
		throw new CustomAccessDeniedException ("접근 권한이 없습니다. (필요 권한 : "+String.join(",", allowedRanks.value())+")");
	}
	
	public static class CustomAccessDeniedException  extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public CustomAccessDeniedException (String message) {
			super(message);
		}
	}
}
