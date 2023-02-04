package com.myclass.entity;

public class Token {
	int id;
	int userId;
	String refreshToken;
	String expiresIn;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}
	
	public Token() {
		super();
	}
	public Token(int id, int userId, String refreshToken, String expiresIn) {
		this.id = id;
		this.userId = userId;
		this.refreshToken = refreshToken;
		this.expiresIn = expiresIn;
	}
	
	
}
