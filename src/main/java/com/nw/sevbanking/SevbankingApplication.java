package com.nw.sevbanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc 
@SpringBootApplication
public class SevbankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SevbankingApplication.class, args);
	}

}
