package com.springboot.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.springboot.Entity.User;
import com.springboot.Repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImp implements UserService{

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public User saveUser(User user) {
		// TODO Auto-generated method stub
		User newuser = userRepo.save(user);
		return newuser;
	}

	@Override
	public void removeSessionMessage() {
		// TODO Auto-generated method stub
		HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest().getSession();
		session.removeAttribute("msg");
	}

}
