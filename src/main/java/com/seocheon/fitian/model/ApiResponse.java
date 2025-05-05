package com.seocheon.fitian.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {

	private boolean success;
	private String message;
	private T data;
	
	public static <T> ApiResponse<T> success(T data){
		ApiResponse<T> res = new ApiResponse<>();
		res.success = true;
		res.data = data;
		return res;
	}
	
	public static <T> ApiResponse<T> success(T data,String message){
		ApiResponse<T> res = new ApiResponse<>();
		res.success = true;
		res.data = data;
		res.message = message;
		return res;
	}
	
	public static <T> ApiResponse<T> failure(String message){
		ApiResponse<T> res = new ApiResponse<>();
		res.success = false;
		res.message = message;
		return res;
	}
	
	public ApiResponse<T> setMessage(String message){
		this.message = message;
		return this;
	}

	public static <T> ApiResponse<T> error(String message){
		ApiResponse<T> res = new ApiResponse<>();
		res.success = false;
		res.data = null;
		res.message = message;
		return res;
	}
	
	public static <T> ApiResponse<T> error(T data,String message){
		ApiResponse<T> res = new ApiResponse<>();
		res.success = false;
		res.data = data;
		res.message = message;
		return res;
	}
}
