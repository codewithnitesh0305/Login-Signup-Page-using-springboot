package com.springboot.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.Entity.User;
import com.springboot.Repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserRepository userRepo;
	
	@GetMapping("/profile")
	public String profile() {
		return "admin_profile";
	}
	
	@ModelAttribute
	public void commonUser(Principal principal , Model model) {
		if (principal != null) {
			String email = principal.getName();
			User user = userRepo.findByEmail(email);
			model.addAttribute("user",user);
		}
	}
}
