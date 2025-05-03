package com.seocheon.fitian.auth.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.seocheon.fitian.model.MemberModel;
import com.seocheon.fitian.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberService memberService;
	
	@Override
	public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
		MemberModel member = memberService.findByUid(uid);
		
		if(member == null) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다. "+uid);
		}
		return new CustomUserDetails(member);
	}
}
