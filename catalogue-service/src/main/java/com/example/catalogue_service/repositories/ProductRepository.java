package com.example.catalogue_service.repositories;

import java.util.List;
import java.util.Optional;

import com.example.catalogue_service.entities.Product;

public interface ProductRepository {

	List<Product> findAll();

	Product save(Product product);

	public Optional<Product> findById(Integer id);
	
	void deleteById(Integer id);

}
