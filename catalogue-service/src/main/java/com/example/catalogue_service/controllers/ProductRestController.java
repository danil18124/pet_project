package com.example.catalogue_service.controllers;

import java.util.Locale;
import java.util.NoSuchElementException;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.catalogue_service.entities.Product;
import com.example.catalogue_service.payloads.UpdateProductPayload;
import com.example.catalogue_service.services.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("catalogue-api/products/{productId:\\d+}")
@RequiredArgsConstructor
public class ProductRestController {
	private final ProductService productService;

	private final MessageSource messageSource;

	@ModelAttribute("product")
	public Product product(@PathVariable("productId") int productId) {
		return this.productService.findProduct(productId)
				.orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
	}

	@GetMapping
	public Product findProduct(@ModelAttribute("product") Product product) {
		return product;
	}

	@PatchMapping
	public ResponseEntity<Void> updateProduct(@PathVariable("productId") int productId,
			@Valid @RequestBody UpdateProductPayload payload, BindingResult bindingResult) throws BindException {
		if (bindingResult.hasErrors()) {
			if (bindingResult instanceof BindException exception) {
				throw exception;
			} else {
				throw new BindException(bindingResult);
			}
		} else {
			this.productService.updateProduct(productId, payload.title(), payload.details());
			return ResponseEntity.noContent().build();
		}
	}

	
	
	@DeleteMapping 
	public ResponseEntity<Void> deleteProduct(@PathVariable("productId") int productId) {
		this.productService.deleteProduct(productId);
		return ResponseEntity.noContent().build();
	}

	
	// обработка ошибки, когда нет товара
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ProblemDetail> handleNoSuchElementxception(NoSuchElementException exception, Locale locale) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND) .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, this.messageSource.getMessage(exception.getMessage(), new Object[0], exception.getMessage(), locale)));
	}

	
}

