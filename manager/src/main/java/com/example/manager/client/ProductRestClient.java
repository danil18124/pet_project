package com.example.manager.client;

import java.util.List;
import java.util.Optional;

import com.example.manager.entities.Product;


public interface ProductRestClient {
	List<Product> findAllProducts();

	Product createProduct(String title, String details);

	Optional<Product> findProduct(Integer id);

	void updateProduct(Integer id, String title, String details);
	
	void deleteProduct(Integer productId);
}
