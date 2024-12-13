package com.example.manager.client;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.example.manager.entities.Product;
import com.example.manager.payload.NewProductPayload;
import com.example.manager.payload.UpdateProductPayload;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestClientProductsRestClient implements ProductRestClient {
	
	private final RestClient restClient;
	
	private static final ParameterizedTypeReference<List<Product>> PRODUCTS_TYPE_REFERENCE = new ParameterizedTypeReference<>(){};
	
	@Override
	public List<Product> findAllProducts() {
		return this.restClient
				.get()
				.uri("/catalogue-api/products")
				.retrieve()
				.body(PRODUCTS_TYPE_REFERENCE);
	}
	
	@Override
	public Product createProduct(String title, String details) {
		try {
			return this.restClient
					.post()
					.uri("/catalogue-api/products")
					.contentType(MediaType.APPLICATION_JSON)
					.body(new NewProductPayload(title, details))
					.retrieve()
					.body(Product.class);
		} catch (HttpClientErrorException.BadRequest exception) {
			ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
		}
	}
	
	@Override
	public Optional<Product> findProduct(Integer id) {
		try {
			return Optional.ofNullable(this.restClient
					.get()
					.uri("/catalogue-api/products/{productId}", id)
					.retrieve()
					.body(Product.class));
		} catch (HttpClientErrorException.NotFound e) {
			return Optional.empty();
		}
	}
	
	@Override
	public void updateProduct(Integer id, String title, String details) {
		try {
			this.restClient
					.patch()
					.uri("/catalogue-api/products")
					.contentType(MediaType.APPLICATION_JSON)
					.body(new UpdateProductPayload(title, details))
					.retrieve()
					.toBodilessEntity();
		} catch (HttpClientErrorException.BadRequest exception) {
			ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
		}
	}


	@Override
	public void deleteProduct(Integer productId) {
		try {
			this.restClient
				.delete()
				.uri("/catalogue-api/products/{productId}", productId)
				.retrieve()
				.toBodilessEntity();
		} catch (HttpClientErrorException.NotFound e) {
			throw new NoSuchElementException(e);
		}
	}


	

}
