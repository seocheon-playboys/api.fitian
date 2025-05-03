package com.seocheon.fitian.auth.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.seocheon.fitian.auth.JwtTokenProvider;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private final JwtTokenProvider jwtTokenProvider;
	private final CustomUserDetailsService customUserDetailsService;
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		String token = resolveToken(request);

		try {
			if(token != null && jwtTokenProvider.validate(token)) {
				String uid = jwtTokenProvider.getUid(token);

				UserDetails userDetails = customUserDetailsService.loadUserByUsername(uid);
				UsernamePasswordAuthenticationToken authenticaion = 
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				SecurityContextHolder.getContext().setAuthentication(authenticaion);
			}
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			handleJwtException(response,"AccessToken이 만료되었습니다.", HttpServletResponse.SC_UNAUTHORIZED);
		} catch (JwtException | IllegalArgumentException e) {
			handleJwtException(response,"유효하지 않은 토큰입니다.", HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
	private String resolveToken(HttpServletRequest request) {
		String bearer = request.getHeader("Authorization");
		if(bearer != null && bearer.startsWith(bearer)) {
			return bearer.substring(7);
		}
		return null;
	}
	
	private void handleJwtException(HttpServletResponse response, String message, int statusCode) throws IOException {
	    response.setStatus(statusCode);
	    response.setContentType("application/json;charset=UTF-8");
	    response.getWriter().write(String.format("{\"message\": \"%s\"}", message));
	}
}
