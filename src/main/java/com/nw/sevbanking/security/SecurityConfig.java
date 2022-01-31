package com.nw.sevbanking.security;

import org.springframework.beans.factory.annotation.Autowired;
import com.nw.sevbanking.security.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
//@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider authProvider;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.httpBasic().and().authorizeRequests().antMatchers("/api/v1/giroaccounts/*/transaction").authenticated().and()
//                .csrf().disable().headers().frameOptions().disable();
//        
        http.authorizeHttpRequests().antMatchers("/**").permitAll().and().csrf().disable();
    }
}