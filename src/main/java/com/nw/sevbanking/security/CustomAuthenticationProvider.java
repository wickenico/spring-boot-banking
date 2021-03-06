package com.nw.sevbanking.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.nw.sevbanking.database.CustomerDTO;
import com.nw.sevbanking.repository.ICustomerRepository;

/**
 * Authentication Settings for HTTP Connection
 * 
 * @author nicowickersheim
 *
 */

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private ICustomerRepository customerRepository;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String id = authentication.getName().toString();
		String pin = authentication.getCredentials().toString();

		try {
			// Check if the customer passed in Username exists
			CustomerDTO customer = customerRepository.findById(Long.parseLong(id)).orElseThrow();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new BadCredentialsException(id);
		}

		Collection<? extends GrantedAuthority> authorities = Collections
				.singleton(new SimpleGrantedAuthority("ROLE_CUSTOMER"));

		return new UsernamePasswordAuthenticationToken(id, pin, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
