package com.example.catalogue_service.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.catalogue_service.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Integer>{
	
	// select * from catalogue.t_product where c_title like:filter
	Iterable<Product> findAllByTitleLikeIgnoreCase(String filter); 

}
