package com.seocheon.fitian.auth;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.seocheon.fitian.auth.dto.JwtTokenPair;
import com.seocheon.fitian.model.MemberModel;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String SECRET_KEY;
	private static final long ACCESS_EXPIRE_TIME_MS = 1000 * 60 * 60 * 1;
	private static final long REFRESH_EXPIRE_TIME_MS = 1000 * 60 * 60 * 24;
	
	
	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	}
	
	public String createToken(MemberModel member) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + ACCESS_EXPIRE_TIME_MS);
		
		return Jwts.builder()
				.setSubject(member.getUid())
				.claim("rank", member.getRank())
				.claim("boxCode", member.getBoxCode())
				.setIssuedAt(now)
				.setExpiration(expiry)
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public JwtTokenPair createTokenPair(MemberModel member) {
		Date now = new Date();
		
		String accessToken = Jwts.builder()
				.setSubject(member.getUid())
				.claim("rank", member.getRank())
				.claim("boxCode", member.getBoxCode())
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime()+ACCESS_EXPIRE_TIME_MS))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
		
		String refreshToken = Jwts.builder()
				.setSubject(member.getUid())
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime()+REFRESH_EXPIRE_TIME_MS))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
		
		return new JwtTokenPair(accessToken, refreshToken);
	}
	
	public boolean validate(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
			return true;
		}catch(ExpiredJwtException  e) {
			log.warn("AccessToken expired: {}", e.getMessage());
		}catch (JwtException | IllegalArgumentException e) {
			log.warn("Invalid JWT token: {}", e.getMessage());
		}
		return false;
	}
	
	public String getUid(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
}
