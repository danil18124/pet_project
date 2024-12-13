package com.example.manager.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.example.manager.client.RestClientProductsRestClient;

import lombok.Value;

@Configuration
public class ClientBeans {
	// Рекомендация от разработчиков SpringFramework: указывать в качестве
	// возвращаемого значения не интерфейс, который реализует класс, а сам класс.
	// Это делается для взаимодействия с GraalVM
	@Bean
	public RestClientProductsRestClient productsRestClient() {
		return new RestClientProductsRestClient(RestClient
				.builder()
				.baseUrl("http://localhost:8081") 
				.build()); 
	}

}
