package com.springboot.Service;

import com.springboot.Entity.User;


public interface UserService {

	public User saveUser(User user);
	public void removeSessionMessage();
}
