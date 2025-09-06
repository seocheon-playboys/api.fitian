package com.seocheon.fitian.auth.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private final JwtTokenProvider jwtTokenProvider;
	private final CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		String token = resolveToken(request);

		try {
			if (token != null) {
				// 빠른 형식 검사: JWT는 'header.payload.signature' 로 '.'이 2개 있어야 함
				long dotCount = token.chars().filter(ch -> ch == '.').count();
				if (dotCount != 2) {
					logger.warn("Incoming token is not a JWT (dots={}): masked={}", dotCount, mask(token));
					// JWT 형식이 아니면 인증 시도하지 않고 다음 필터로 넘김
					filterChain.doFilter(request, response);
					return;
				}

				// 검증 시도
				if (jwtTokenProvider.validate(token)) {
					String uid = jwtTokenProvider.getUid(token);
					UserDetails userDetails = customUserDetailsService.loadUserByUsername(uid);

					UsernamePasswordAuthenticationToken authentication =
							new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}

			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			logger.info("Expired JWT for request: {}", mask(token));
			handleJwtException(response, "AccessToken이 만료되었습니다.", HttpServletResponse.SC_UNAUTHORIZED);
		} catch (UsernameNotFoundException e) {
			logger.warn("User not found during JWT authentication: {}, maskedToken={}", e.getMessage(), mask(token));
			handleJwtException(response, "사용자를 찾을 수 없습니다.", HttpServletResponse.SC_UNAUTHORIZED);
		} catch (JwtException | IllegalArgumentException e) {
			logger.warn("Invalid JWT token for request: {}, cause: {}", mask(token), e.getMessage());
			handleJwtException(response, "유효하지 않은 토큰입니다.", HttpServletResponse.SC_UNAUTHORIZED);
		} catch (Exception e) {
			// 예기치 않은 예외는 500으로 처리하거나 로그만 남기고 다음 필터로 넘길지 결정
			logger.error("Unexpected error in JwtAuthenticationFilter", e);
			handleJwtException(response, "서버 인증 처리 중 오류가 발생했습니다.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private String resolveToken(HttpServletRequest request) {
		String bearer = request.getHeader("Authorization");
		if (bearer != null) {
			// 대소문자 Bearer 허용
			if (bearer.startsWith("Bearer ")) {
				return bearer.substring(7);
			}
			if (bearer.startsWith("bearer ")) {
				return bearer.substring(7);
			}
		}
		return null;
	}

	private void handleJwtException(HttpServletResponse response, String message, int statusCode) throws IOException {
		response.setStatus(statusCode);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(String.format("{\"message\": \"%s\"}", message));
	}

	private String mask(String token) {
		if (token == null) return null;
		if (token.length() <= 12) return token.replaceAll(".", "*");
		return token.substring(0, 6) + "..." + token.substring(token.length() - 4);
	}
}
