package com.seocheon.fitian.auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seocheon.fitian.auth.dto.JwtToken;
import com.seocheon.fitian.auth.dto.JwtTokenPair;
import com.seocheon.fitian.auth.dto.LoginResponseDto;
import com.seocheon.fitian.mapper.MemberMapper;
import com.seocheon.fitian.model.ApiResponse;
import com.seocheon.fitian.model.MemberModel;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:8088")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	@Autowired
	private OAuthService oAuthService;
	private final RefreshTokenService refreshTokenService;
	private final JwtTokenProvider jwtTokenProvider;
	private final MemberMapper memberMapper;
	
	@PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> loginWithSocial(
    		@RequestHeader("Authorization") String socialTokenHeader,
    		@RequestParam("Provider") String provider ){
		
		String token = socialTokenHeader.replace("Bearer ", "");
		
		MemberModel member = oAuthService.findOrCreateUserBySocialToken(token, provider);
		boolean isNewUser = member.getRank() == null;
		
		JwtTokenPair pair = jwtTokenProvider.createTokenPair(member);
		
		refreshTokenService.create(member.getUid(), pair.getRefreshToken());
		
		LoginResponseDto responseDto = new LoginResponseDto(pair, member, isNewUser);
		
    	return ResponseEntity.ok(ApiResponse.success(responseDto));
    }
	
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshAccessToken(
			@RequestHeader("Authorization") String refreshHeader
			) {
		
		String refreshToken = refreshHeader.replace("Bearer ", "");
		
		try {
			
			if(!jwtTokenProvider.validate(refreshToken)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("message","유효하지 않은 RefreshToken입니다."));
			}
			
			String uid = jwtTokenProvider.getUid(refreshToken);
			
			String savedToken = refreshTokenService.findByUid(uid);
			if(savedToken == null || !savedToken.equals(refreshToken)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("message","저장된 RefreshToken과 일치하지 않습니다."));
			}
			
			MemberModel member = memberMapper.findByUid(uid);
			if(member == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("message","사용자를 찾을 수 없습니다."));
			}
			
			String newAccessToken = jwtTokenProvider.createToken(member);
			
			return ResponseEntity.ok(new JwtToken(newAccessToken));
			
		} catch (ExpiredJwtException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("message","RefreshToken이 만료되었습니다. 다시 로그인해주세요."));
		} catch (JwtException | IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("message","유효하지 않은 RefreshToken입니다."));
		}
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(
			@RequestHeader("Authorization") String refreshHeader
			) {
		if(refreshHeader == null || !refreshHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰이 없습니다.");
		}
		
		String refreshToken = refreshHeader.replace("Bearer ", "");
		
		if(!jwtTokenProvider.validate(refreshToken)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
		}
		
		String uid = jwtTokenProvider.getUid(refreshToken);
		
		refreshTokenService.delete(uid);
		
		return ResponseEntity.ok("로그아웃 완료");
	}
}
