package com.example.catalogue_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityBeans {
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.
				authorizeHttpRequests(authorizeHttpRequests -> 
					authorizeHttpRequests.requestMatchers("/catalogue-api/**")
					.hasRole("SERVICE"))
				.httpBasic(Customizer.withDefaults()) // включение basic аутентификации
				.sessionManagement(sessionManagement -> 
					sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.build();	
	}
}
