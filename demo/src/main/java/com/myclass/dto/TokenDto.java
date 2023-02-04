package com.myclass.dto;

import com.myclass.entity.User;

public class TokenDto {
	private UserDto user;
	private String token;
	private String refreshToken;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public UserDto getUser() {
		return user;
	}
	public void setUser(UserDto user) {
		this.user = user;
	}
	
	public TokenDto() {}
	
	public TokenDto(UserDto user,String token, String refreshToken) {	
		this.user = user;
		this.token = token;
		this.refreshToken = refreshToken;
		
	}
	
}
