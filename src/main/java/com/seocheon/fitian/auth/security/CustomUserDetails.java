package com.seocheon.fitian.auth.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.seocheon.fitian.model.MemberModel;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	private final MemberModel member;
	
	public CustomUserDetails(MemberModel member) {
		this.member = member;
	}
	
	public MemberModel getMember() {
		return member;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}
	
	@Override
	public String getPassword() {
		return "";
	}
	
	@Override
	public String getUsername() {
		return member.getUid();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return "Y".equalsIgnoreCase(member.getUseYN());
	}
}
