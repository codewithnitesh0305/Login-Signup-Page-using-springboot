package com.springboot.Service;

import java.util.Date;
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
import jakarta.transaction.Transactional;

@Service
@Transactional
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
		
		//When user is register than field Enable set 0 and field verficationcode will set unique code 
		user.setEnable(false);
		user.setVerficationCode(UUID.randomUUID().toString());
		
		user.setAccountLocked(true);
		user.setFailedAttempt(0);
		user.setLockTime(null);
		
		
		User newuser = userRepo.save(user);
		//If user is registered than mail will send 
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
				helper.setSubject(subject);
				
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
			user.setEnable(true); //If user is verified than this field set 1
			user.setVerficationCode(null); //and this field set null
			userRepo.save(user);
			return true;
		}
	}
	
	
	private static final long lock_duration_time = 30000; //5 Second
	public static final long ATTEMPT_TIME = 3;
	
	//Every time when user fill the wrong password than it will increase by 1
	@Override
	public void increaseFaildAttempt(User user) {
		// TODO Auto-generated method stub
		int attempt = user.getFailedAttempt()+1;
		userRepo.updateFailedAttempt(attempt,user.getEmail());
	}

	//If user login successfully or the time is over the it will reset 
	@Override
	public void resetAttempt(String email) {
		// TODO Auto-generated method stub
		userRepo.updateFailedAttempt(0,email);
	
	}

	//If user try to login more the 3 attempt than account will lock 
	@Override
	public void lock(User user) {
		// TODO Auto-generated method stub
		user.setAccountLocked(false); //account will lock
		user.setLockTime(new Date()); //when account is lock that time will save in database
		userRepo.save(user);
	}

	@Override
	public boolean unlockAccountTimeExpired(User user) {
		// TODO Auto-generated method stub
		long LockTimeInMills = user.getLockTime().getTime(); 
		long currentTimeMillis = System.currentTimeMillis();
		if(LockTimeInMills + lock_duration_time < currentTimeMillis) {
			user.setAccountLocked(true);
			user.setLockTime(null);
			user.setFailedAttempt(0);
			userRepo.save(user);
			return true;
		}
		return false;
	}
	
	

}
