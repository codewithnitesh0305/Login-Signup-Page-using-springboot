package com.springboot.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.springboot.Entity.User;
import com.springboot.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	
	  @Autowired 
	  private UserService userservice;
	 
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	@GetMapping("/home")
	public String home() {
		return "home";
	}
	@GetMapping("/profile")
	public String profile() {
		return "profile";
	}
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	//Registration for new User
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute User user , HttpSession session) {
		//System.out.println(user);
		User u = userservice.saveUser(user);
		if(u != null) {
			session.setAttribute("msg","Register Successfully");
			//System.out.println("Save Success");
		}else {
			session.setAttribute("msg","Register Successfully");
			//System.out.println("Error");
		}
		return "redirect:/register";
	}
}
