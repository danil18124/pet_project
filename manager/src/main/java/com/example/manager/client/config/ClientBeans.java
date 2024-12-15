package com.example.manager.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestClient;

import com.example.manager.client.RestClientProductsRestClient;


@Configuration
public class ClientBeans {
	// Рекомендация от разработчиков SpringFramework: указывать в качестве
	// возвращаемого значения не интерфейс, который реализует класс, а сам класс.
	// Это делается для взаимодействия с GraalVM
	@Bean
    public RestClientProductsRestClient productsRestClient(
            @Value("${pet.services.catalogue.uri:http://localhost:8081}") String catalogueBaseUri, 
            @Value("${pet.services.catalogue.username:catalogue_service_user}") String catalogueUsername, 
            @Value("${pet.services.catalogue.password:password}") String cataloguePassword) {
        return new RestClientProductsRestClient(RestClient.builder()
                .baseUrl(catalogueBaseUri)
                .requestInterceptor(
                        new BasicAuthenticationInterceptor(catalogueUsername, cataloguePassword))
                .build());
    }


}
