package com.myclass.commons;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class Validation {
	public boolean EmailValidation(String email) {
		String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	public boolean PasswordValidation(String email) {
		if (email.length()>=8 && email.length() <=20) {
			return true;
		}else {
			return false;
		}
	}
}
