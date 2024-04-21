package com.springboot.Service;

import com.springboot.Entity.User;


public interface UserService {

	public User saveUser(User user, String url);
	public void removeSessionMessage();
	
	public void sendEmail(User user, String url);
	public boolean verifyAccount(String verficationCode);
	
	public void increaseFaildAttempt(User user);
	public void resetAttempt(String email);
	public void lock(User user);
	public boolean unlockAccountTimeExpired(User user);
}
