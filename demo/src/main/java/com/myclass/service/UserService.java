package com.myclass.service;

import com.myclass.entity.ServiceInfo;
import com.myclass.entity.User;

public interface UserService {
	User getUserById(int id);
	User getLastUser();
	User getUserByEmail(String email);
	ServiceInfo add(User user);
	
}
