package com.example.manager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.manager.entities.Product;
import com.example.manager.payload.NewProductPayload;
import com.example.manager.services.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products")
public class ProductsController {
	private final ProductService productService;
	
	@GetMapping("list")
	public String getProductsList(Model model) {
		model.addAttribute("products", this.productService.findAllProducts());
		return "catalogue/products/list";
	}
	
	@GetMapping("create")
	public String getNewProductPage() {
		return "catalogue/products/create";
	}
	
	@PostMapping("create")
	public String createProduct(@Valid @ModelAttribute NewProductPayload productPayload, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("error", bindingResult.getAllErrors().stream()
					.map(ObjectError::getDefaultMessage)
					.toList());
			return "catalogue/products/new_product";
		} else {
			Product product = this.productService.createProduct(productPayload.title(), productPayload.details());
			return "redirect:/catalogue/products/%/d".formatted(product.getId());
		}
		
	}

}
