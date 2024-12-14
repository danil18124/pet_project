package com.example.catalogue_service.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.catalogue_service.entities.Product;
import com.example.catalogue_service.repositories.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService{
	private final ProductRepository productRepository;
	
	@Override
	public Iterable<Product> findAllProducts(String filter) {
		if(filter != null && !filter.isBlank()) {
			return this.productRepository.findAllByTitleLikeIgnoreCase(filter);
		} else {
			return this.productRepository.findAll();
		}
	}
	
	@Override
	@Transactional
	public Product createProduct(String title, String details) {
		return this.productRepository.save(new Product(null, title, details));
	}
	
	@Override
	public Optional<Product> findProduct(Integer id) {
		return this.productRepository.findById(id);
	}
	
	@Override
	@Transactional
	public void updateProduct(Integer id, String title, String details) {
		this.productRepository.findById(id).ifPresentOrElse(product -> {
			product.setTitle(title);
			product.setDetails(details);
		}, () -> {
			throw new NoSuchElementException();
		});

	}
	
	@Override
	@Transactional
	public void deleteProduct(Integer productId) {
		this.productRepository.deleteById(productId);
		
	}
	
}
