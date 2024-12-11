package com.example.manager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.manager.entities.Product;
import com.example.manager.payload.UpdateProductPayload;
import com.example.manager.services.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products/{productId:\\d+}")
public class ProductController {
	
	private final ProductService productService;
	
	@ModelAttribute("product")
	Product product(@PathVariable("productId") Integer id, Model model) {
		return this.productService.findProduct(id).orElseThrow();
	}
	
	@GetMapping
	public String getProductPage(@PathVariable("productId") Integer id, Model model) {
		return "catalogue/products/product_page";
	}
	
	@GetMapping("edit")
	public String getProductEditPage(@PathVariable("productId") Integer id, Model model) {
		return "catalogue/products/edit_page";
	}
	
	@PostMapping("edit")
	public String editProduct(@ModelAttribute("product") Product product, UpdateProductPayload payload) {
		this.productService.updateProduct(product.getId(), payload.title(), payload.details());
		return "redirect:/catalogue/products/%d".formatted(product.getId());
	}
	
	@PostMapping("delete")
	public String deleteProduct(@ModelAttribute("product") Product product) {
		this.productService.deleteProduct(product.getId());
		return "redirect:/catalogue/products/list";
	}
}
