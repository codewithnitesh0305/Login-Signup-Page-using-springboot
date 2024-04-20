package com.springboot.Service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.springboot.Entity.User;
import com.springboot.Repository.UserRepository;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImp implements UserService{

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private BCryptPasswordEncoder passwrodEncoder;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public User saveUser(User user , String url) {
		// TODO Auto-generated method stub
		String hashCode = passwrodEncoder.encode(user.getPassword()); 
		user.setPassword(hashCode);
		user.setRole("ROLE_USER");
		
		user.setEnable(false);
		user.setVerficationCode(UUID.randomUUID().toString());
		
		
		User newuser = userRepo.save(user);
		if(newuser != null) {
			sendEmail(newuser,url);
		}
		return newuser;
	}

	@Override
	public void removeSessionMessage() {
		// TODO Auto-generated method stub
		HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest().getSession();
		session.removeAttribute("msg");
	}

	@Override
	public void sendEmail(User user, String url) {
		// TODO Auto-generated method stub
		String form = "nk685602@gmail.com";
		String to = user.getEmail();
		String subject = "Account Verfication";
		String content = "Dear [[name]],<br>"+"Please click the link below to verify your your registration: <br>"
		+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"+"Thank You";
		
		try {
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message);
				System.out.println();
				helper.setFrom(form,"Nitesh Kumar");
				helper.setTo(to);
				
				content = content.replace("[[name]]",user.getUsername());
				String siteUrl = url + "/verify?code="+user.getVerficationCode();
				content = content.replace("[[URL]]",siteUrl);
				helper.setText(content,true);
				mailSender.send(message);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public boolean verifyAccount(String verficationCode) {
		// TODO Auto-generated method stub
		User user = userRepo.findByVerficationCode(verficationCode);
		if(user == null) {
			return false;
			
		}else {
			user.setEnable(true);
			user.setVerficationCode(null);
			userRepo.save(user);
			return true;
		}
	}
	
	

}
