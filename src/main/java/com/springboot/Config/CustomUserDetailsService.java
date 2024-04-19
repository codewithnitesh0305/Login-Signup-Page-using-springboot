package com.springboot.Config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.springboot.Repository.UserRepository;
import com.springboot.Entity.User;
@Component
public class CustomUserDetailsService implements UserDetailsService{
//2 Retrieve the user email from the database and than send to the custom user class
	@Autowired
	private UserRepository userRepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepo.findByEmail(username);
		if(user  == null) {
			throw new UsernameNotFoundException("User not found");
		}else {
			return new CustomUser(user);
		}
		
	}

}
