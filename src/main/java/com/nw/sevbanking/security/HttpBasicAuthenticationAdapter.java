//package com.nw.sevbanking.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//@EnableWebSecurity
//public class HttpBasicAuthenticationAdapter extends WebSecurityConfigurerAdapter {
////	@Autowired
////	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
////		auth.inMemoryAuthentication().withUser("giroaccountId").password(passwordEncoder().encode("pin")).roles("");
////	}
//	
//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
////    	httpSecurity
////        .httpBasic()
////    .and()
////        .authorizeRequests()
//////        .antMatchers("/v1/giroaccounts").authenticated()
//////        .antMatchers("/v1/creditcards").authenticated()
//////        .antMatchers("/v1/users").permitAll()
////        .anyRequest().permitAll()
////        .and().csrf().disable();
//
//    	httpSecurity.authorizeHttpRequests().antMatchers("/**").permitAll().and().csrf().disable();
//        
//    }
//	
////    @Bean
////    public static PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
//
//	
//}