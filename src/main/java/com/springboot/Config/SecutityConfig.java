package com.springboot.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecutityConfig {
//3
	@Autowired
	public CustomAuthSuccessHaldler SuccessHandler;
	@Autowired
	public CustomeFailerHandler FailierHandler;
	
	//This method will Convert the simple string password into hash code
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//This method is retrieve the user data form the database
	@Bean
	public UserDetailsService getDetailsService() {
		return new CustomUserDetailsService();
	}
	//The DaoAuthenticationProvider class in Spring Security is a core component 
	//responsible for performing authentication based on user details retrieved from a data access object (DAO).
	@Bean
	public DaoAuthenticationProvider getAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(getDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	@Bean
	public SecurityFilterChain filter(HttpSecurity http) throws Exception {
//		http.csrf().disable().
//		authorizeHttpRequests().requestMatchers("/","/register","/login","/saveUser").permitAll() //This line will allow us which page we can access
//		.requestMatchers("/user/**").authenticated().and() //This line will not allow to access the url user/profile and user/home
//		.formLogin().loginPage("/login").loginProcessingUrl("/userLogin")
//		.defaultSuccessUrl("/user/profile").permitAll();
		
//		http.csrf().disable()
//		.authorizeHttpRequests().requestMatchers("/user/**").hasRole("USER")
//		.requestMatchers("/admin/**").hasRole("ADMIN")
//		.requestMatchers("/**").permitAll().and()
//		.formLogin().loginPage("/login").loginProcessingUrl("/userLogin")
//		.successHandler(customeAuthSuccessHandler)
//		.and().logout().permitAll();
		
		 http
         .csrf().disable()
         .authorizeRequests()
             .requestMatchers("/user/**").hasRole("USER")
             .requestMatchers("/admin/**").hasRole("ADMIN")
             .requestMatchers("/**").permitAll()
             .and()
         .formLogin()
             .loginPage("/login")
             .loginProcessingUrl("/userLogin")
             .failureHandler(FailierHandler)
             .successHandler(SuccessHandler)
             .permitAll();

		return http.build();
	}
	
	
}
