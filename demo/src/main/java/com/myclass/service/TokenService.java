package com.myclass.service;

import com.myclass.entity.ServiceInfo;
import com.myclass.entity.Token;

public interface TokenService {
	Token getToken(String refreshToken);
	ServiceInfo delete(int userId);
	ServiceInfo add(Token token);
	ServiceInfo refresh(Token token);
}
