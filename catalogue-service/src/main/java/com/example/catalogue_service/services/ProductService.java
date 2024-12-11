package com.example.catalogue_service.services;

import java.util.List;
import java.util.Optional;

import com.example.catalogue_service.entities.Product;


public interface ProductService {

	List<Product> findAllProducts();

	Product createProduct(String title, String details);

	Optional<Product> findProduct(Integer id);

	void updateProduct(Integer id, String title, String details);
	
	void deleteProduct(Integer productId);

}