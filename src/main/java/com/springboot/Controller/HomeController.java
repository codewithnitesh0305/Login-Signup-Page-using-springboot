package com.springboot.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.springboot.Entity.User;
import com.springboot.Repository.UserRepository;
import com.springboot.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	
	  @Autowired 
	  private UserService userservice;
	  @Autowired
	 private UserRepository userRepo;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	@GetMapping("user/home")
	public String home() {
		return "home";
	}
//	@GetMapping("user/profile")
//	public String profile(Principal principal, Model model) {
//		String email = principal.getName();
//		User user = userRepo.findByEmail(email);
//		model.addAttribute("user",user);
//		return "profile";
//	}
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
	public String saveUser(@ModelAttribute User user , HttpSession session, HttpServletRequest request) {
		//System.out.println(user);
		String url = request.getRequestURL().toString(); 
		//System.out.println(url); //http://localhost:8080/saveUser
		url = url.replace(request.getServletPath(),"");
		//System.out.println(url); //http://localhost:8080
		
		User u = userservice.saveUser(user , url);
		if(u != null) {
			session.setAttribute("msg","Register Successfully");
			//System.out.println("Save Success");
		}else {
			session.setAttribute("msg","Register Successfully");
			//System.out.println("Error");
		}
		return "redirect:/register";
	}
	
	//For Create dynamic nav bar
	@ModelAttribute
	public void commonUser(Principal principal, Model model) {
		if( principal != null) {
			String email = principal.getName();
			User user = userRepo.findByEmail(email);
			model.addAttribute("user",user);
		}
		
	}
	
	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code, Model model) {
		boolean f = userservice.verifyAccount(code);
		if(f) {
			model.addAttribute("message","Successfully your account is verified");
		}else {
			model.addAttribute("message","Your verifycation code may be incorrect or already verified");
		}
		return "message";
	}
	
}
