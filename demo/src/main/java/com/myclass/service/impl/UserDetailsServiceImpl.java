package com.myclass.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.myclass.entity.User;
import com.myclass.repository.UserMapper;
import com.myclass.until.CustomUserDetails;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	
	@Autowired
	UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userMapper.findByEmail(email);
		if(user == null) {
			throw new UsernameNotFoundException("Not found acount");
		}
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		

		CustomUserDetails userDetails = new CustomUserDetails(user.getEmail(), 
				user.getPassword(), authorities);
		userDetails.setEmail(user.getEmail());
		return userDetails;
	}

}
