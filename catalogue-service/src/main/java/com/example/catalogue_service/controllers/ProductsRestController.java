package com.example.catalogue_service.controllers;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.catalogue_service.entities.Product;
import com.example.catalogue_service.payloads.NewProductPayload;
import com.example.catalogue_service.services.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/products")
public class ProductsRestController {

	private final ProductService productService;
	private final MessageSource messageSource;
	
	
	@GetMapping
    public Iterable<Product> findProducts(@RequestParam(name = "filter", required = false) String filter) {
        return this.productService.findAllProducts(filter);
    }
	
	
	@PostMapping()
	public ResponseEntity<?> createProduct(@Valid @RequestBody NewProductPayload payload, BindingResult bindingResult,
			UriComponentsBuilder uriComponentsBuilder, Locale locale) {
		if (bindingResult.hasErrors()) {
			ProblemDetail problemDetail = ProblemDetail
					.forStatusAndDetail(HttpStatus.BAD_REQUEST,
					this.messageSource.getMessage("errors.400.title", new Object[0], "errors.400.title", locale));
			problemDetail.setProperty("errors",
					bindingResult.getAllErrors().stream()
					.map(ObjectError::getDefaultMessage)
					.toList());
			return ResponseEntity.badRequest().body(problemDetail);
		} else {
			Product product = this.productService.createProduct(payload.title(), payload.details());
			return ResponseEntity
					.created(uriComponentsBuilder.replacePath("/catalogue-api/products/{productId}")
					.build(Map.of("productId", product.getId())))
					.body(product);
		}
	}

}
