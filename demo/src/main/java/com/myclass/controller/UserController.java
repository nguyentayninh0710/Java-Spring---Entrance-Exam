package com.myclass.controller;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myclass.dto.TokenDto;
import com.myclass.dto.UserDto;
import com.myclass.entity.ServiceInfo;
import com.myclass.entity.Token;
import com.myclass.entity.User;
import com.myclass.service.TokenService;
import com.myclass.service.UserService;
import com.myclass.until.CustomUserDetails;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("api/auth")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	int userId;
	
	Authentication authentication;
	
	@RequestMapping(value = "/sign-in", method = RequestMethod.POST)
	public Object post(@RequestBody User user) {		
		try {			
			ServiceInfo info = userService.add(user);
			if(info.isStatus()==true) {				
				info.setMessage(info.getMessage() + "\n" 
			    + "\"firstName\": " +"\""+info.getUser().getFirstName() +"\","+"\n"
			    + "\"lastName\": " +"\""+info.getUser().getLastName() +"\","+"\n"
			    + "\"email\": " +"\""+info.getUser().getEmail() +"\","+"\n"
			    + "\"displayName\": " +"\""+ info.getUser().getFirstName()+" "+info.getUser().getLastName() +"\""
				);
				return new ResponseEntity<String>(info.getMessage(), HttpStatus.OK);
			}
			else {
				return new ResponseEntity<String>(info.getMessage(), HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Object login(@RequestBody User user) {
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
			
			CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			Token refreshToken = createRefreshToken(customUser.getEmail());
			tokenService.add(refreshToken);
			String token = generateToken(authentication);
			TokenDto userToken = new TokenDto();
			User account = userService.getUserByEmail(user.getEmail());
			UserDto accountDto = new UserDto();
			accountDto.setId(account.getId());
			accountDto.setEmail(account.getEmail());
			accountDto.setFirstName(account.getFirstName());
			accountDto.setLastName(account.getLastName());
			accountDto.setDisplayName(account.getFirstName() + account.getLastName());
			userToken.setUser(accountDto);
			userToken.setToken(token);	
			userToken.setRefreshToken(refreshToken.getRefreshToken());
			userId = account.getId();
			return new ResponseEntity<TokenDto>(userToken, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>("Wrong name or pass word", HttpStatus.BAD_REQUEST);
		
	}
	private String generateToken(Authentication authentication) {
		final String JWT_SECRET = "SECRET";
		final long JWT_EXPIRATION = 600000;
		Date now = new Date();
		Date expiryDate = new Date(now.getTime()+JWT_EXPIRATION);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String token = Jwts.builder()
				.setSubject(userDetails.getUsername())
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, JWT_SECRET)
				.compact();
		return token;
	}
	
	private Token createRefreshToken(String email) {
		User user = userService.getUserByEmail(email); 
		Token refreshToken = new Token();
		refreshToken.setUserId(user.getId());
		refreshToken.setExpiresIn("30");
		refreshToken.setRefreshToken(UUID.randomUUID().toString());
		return refreshToken;
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public Object logout() {		
		try {			
			ServiceInfo info = tokenService.delete(userId);
			if(info.isStatus()==true) {	
				info.setMessage("Logout success");
				return new ResponseEntity<String>(info.getMessage(), HttpStatus.OK);
			}
			else {
				info.setMessage("Logout internal error");
				return new ResponseEntity<String>(info.getMessage(), HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@RequestMapping(value = "/refresh-token", method = RequestMethod.POST)
	public Object refreshToken(@RequestBody String refreshToken) {
		try {
			String token = generateToken(authentication);
			Token myToken = tokenService.getToken(refreshToken);
			if(myToken!=null) {
				myToken.setExpiresIn("30");
				ServiceInfo info = tokenService.refresh(myToken);
				if(info.isStatus()==true) {	
					info.setMessage(info.getMessage() + "\n" 
						    + "\"token\": " +"\""+token+"\","+"\n"
						    + "\"refreshToken\": " +"\""+myToken.getRefreshToken() +"\""						   
							);
							return new ResponseEntity<String>(info.getMessage(), HttpStatus.OK);
				}
				else {
					return new ResponseEntity<String>(info.getMessage(), HttpStatus.BAD_REQUEST);
				}
			}else {
				return new ResponseEntity<String>("Supplied refreshToken in the inbound does not exist", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
		}
		
		

		
	}
	
//	@PostMapping("/logout")
//    public ResponseEntity<?> logout(@RequestBody TokenDto dto) {
//        String refreshTokenString = dto.getRefreshToken();
//        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
//            // valid and exists in db
//            refreshTokenRepository.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));
//            return ResponseEntity.ok().build();
//        }
//
//        throw new BadCredentialsException("invalid token");
//    }
	

}
