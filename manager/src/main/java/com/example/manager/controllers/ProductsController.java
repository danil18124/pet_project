package com.example.manager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.manager.client.BadRequestException;
import com.example.manager.client.ProductRestClient;
import com.example.manager.entities.Product;
import com.example.manager.payload.NewProductPayload;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products")
public class ProductsController {
	private final ProductRestClient productRestClient;
	
	@GetMapping("list")
	public String getProductsList(Model model) {
		model.addAttribute("products", this.productRestClient.findAllProducts());
		return "catalogue/products/list";
	}
	
	@GetMapping("create")
	public String getNewProductPage() {
		return "catalogue/products/create";
	}
	
	@PostMapping("create")
	public String createProduct(@ModelAttribute NewProductPayload productPayload, Model model) {
		try {
			Product product = this.productRestClient.createProduct(productPayload.title(), productPayload.details());
			return "redirect:/catalogue/products/%d".formatted(product.id());
		} catch (BadRequestException exception) {
			model.addAttribute("payload", productPayload);
            model.addAttribute("errors", exception.getErrors());
            return "catalogue/products/create";
		}
		
	}

}
