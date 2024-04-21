package com.springboot.Config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.springboot.Entity.User;
import com.springboot.Repository.UserRepository;
import com.springboot.Service.UserService;
import com.springboot.Service.UserServiceImp;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomeFailerHandler extends SimpleUrlAuthenticationFailureHandler{

	@Autowired
	private UserService userservice;
	@Autowired
	private UserRepository userRepo;
	 
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		// TODO Auto-generated method stub
		String email = request.getParameter("username");
		User user = userRepo.findByEmail(email);
		if(user != null) { //Check user in register or not
			if(user.isEnable()) { //check user is verified by link or not
				if(user.isAccountLocked()) { //check account is lock(false) or unlock(true)
					if(user.getFailedAttempt() < UserServiceImp.ATTEMPT_TIME - 1) { //Count the number of attempt user fill wrong password
						userservice.increaseFaildAttempt(user); //Increase the number of attempt
					}else {
						userservice.lock(user); //Account will lock if user attempt more than 3 time
						exception = new LockedException("Your account is locked!  failed attept 3 times");
					}
				}else if(!user.isAccountLocked()) { //this line will run if account is lock and unlock
					if(userservice.unlockAccountTimeExpired(user)) { //Check the time duration is complete or not
						exception = new LockedException("Account is unlocked! Please try to login");
					}else {
						exception = new LockedException("Account is locked! Please try after some time");
					}
				}
			}else {
				exception = new LockedException("Account is inactive verify account");
			}
		}
		super.setDefaultFailureUrl("/login?error");
		super.onAuthenticationFailure(request, response, exception);
	}
	
}
