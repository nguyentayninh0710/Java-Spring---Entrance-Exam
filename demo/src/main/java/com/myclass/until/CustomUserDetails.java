package com.myclass.until;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User implements UserDetails{
	private static final long serialVersionUID = 1L;
	
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public CustomUserDetails(String email, String password, 
			Collection<? extends GrantedAuthority> authorities) {
		super(email, password, authorities);
		// TODO Auto-generated constructor stub
	}
	
}
