package com.example.manager.repositories;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.stereotype.Repository;

import com.example.manager.entities.Product;

@Repository
public class InMemoryProductRepository implements ProductRepository{
	private final List<Product> products = Collections.synchronizedList(new LinkedList<>());
	
	@Override
	public List<Product> findAll() {
		return Collections.unmodifiableList(this.products);
	}
	
	public InMemoryProductRepository() {
		IntStream.range(1, 4)
		.forEach(i -> this.products.add(new Product(i, "Product #%d".formatted(i), "Details of product #%d".formatted(i))));
	}
	
	@Override
	public Product save(Product product) {
		product.setId(
				this.products.stream()
				.max(Comparator.comparingInt(Product::getId))
				.map(Product::getId)
				.orElse(0) 
				+ 1);
		this.products.add(product);
		return product;
	}

	@Override
	public Optional<Product> findById(Integer id) {
		return this.products.stream()
				.filter(product -> Objects.equals(product.getId(), id))
				.findFirst();
	}

	@Override
	public void deleteById(Integer id) {
		this.products.removeIf(product -> Objects.equals(product.getId(), id));
		
	}
}
