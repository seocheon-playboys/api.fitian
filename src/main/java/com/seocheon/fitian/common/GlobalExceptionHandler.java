package com.seocheon.fitian.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.seocheon.fitian.auth.aop.RankAuthorizationAspect.CustomAccessDeniedException;
import com.seocheon.fitian.model.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	//접근 제한
	@ExceptionHandler(CustomAccessDeniedException .class)
	public ResponseEntity<ApiResponse<Void>> handleAccessDenied(CustomAccessDeniedException  ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(ex.getMessage()));
	}
	
	//비즈니스 로직 실패
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ApiResponse<Void>> handleIllegalState(IllegalStateException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(ex.getMessage()));
	}
	
	//예상못한 오류
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(ex.getMessage()));
	}
}
