package com.myclass.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myclass.entity.ServiceInfo;
import com.myclass.entity.Token;
import com.myclass.repository.TokenMapper;
import com.myclass.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService{

	@Autowired
	TokenMapper tokenMapper;
	
	
	@Override
	public Token getToken(String refreshToken) {
		Token token = tokenMapper.findByToken(refreshToken);
		return token;
	}

	@Override
	public ServiceInfo delete(int userId) {
		tokenMapper.delete(userId);
		try {
			tokenMapper.delete(userId);
			return new ServiceInfo(true, "Delete successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return new ServiceInfo(false, "Delete error");
		}
	}

	@Override
	public ServiceInfo add(Token token) {
		try {
			tokenMapper.insert(token);
			return new ServiceInfo(true, "Add successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return new ServiceInfo(false, "Add error");
		}
	}

	@Override
	public ServiceInfo refresh(Token token) {
		try {
			tokenMapper.update(token);
			return new ServiceInfo(true, "Update successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return new ServiceInfo(false, "Update error");
		}
	}

}
