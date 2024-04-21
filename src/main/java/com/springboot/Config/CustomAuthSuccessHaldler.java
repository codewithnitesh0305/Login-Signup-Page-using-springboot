package com.springboot.Config;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.springboot.Entity.User;
import com.springboot.Service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthSuccessHaldler implements AuthenticationSuccessHandler {

	/*
	 * In Spring Security, the AuthenticationSuccessHandler interface is used to
	 * handle successful authentication events. When a user successfully logs in,
	 * Spring Security invokes the appropriate implementation of
	 * AuthenticationSuccessHandler to perform custom actions, such as redirecting
	 * the user to a specific page, logging the event, or setting up session
	 * attributes.
	 */
	@Autowired
	private UserService userservice;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		//If the user will login than the number of attempt set 0
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		User user = customUser.getUser();
		if (user != null) {
			userservice.resetAttempt(user.getEmail());
		}
		
		if (roles.contains("ROLE_ADMIN")) {
			response.sendRedirect("/admin/profile");
		} else {
			response.sendRedirect("/user/profile");
		}

	}

}
