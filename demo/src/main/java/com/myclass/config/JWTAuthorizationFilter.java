package com.myclass.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import io.jsonwebtoken.Jwts;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter{
	
	private UserDetailsService _userDetailsService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, 
			UserDetailsService userDetailsService) {
		super(authenticationManager);
		_userDetailsService = userDetailsService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		final String JWT_SECRET = "SECRET";
		String tokenBearer = request.getHeader("Authorization");
		if(tokenBearer!=null && tokenBearer.startsWith("Bearer ")) {
			String token = tokenBearer.replace("Bearer ", "");
			String username = Jwts.parser()
					.setSigningKey(JWT_SECRET)
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
			UserDetails userDetails = _userDetailsService.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}
		chain.doFilter(request, response);
	}

}
