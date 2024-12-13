package com.example.manager.controllers;

import java.util.Locale;
import java.util.NoSuchElementException;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.manager.client.BadRequestException;
import com.example.manager.client.ProductRestClient;
import com.example.manager.entities.Product;
import com.example.manager.payload.UpdateProductPayload;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products/{productId:\\d+}")
public class ProductController {
	
	private final ProductRestClient productRestClient;
	private final MessageSource messageSource;
	
	@ModelAttribute("product")
	Product product(@PathVariable("productId") Integer id, Model model) {
		return this.productRestClient.findProduct(id).orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
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
    public String updateProduct(@ModelAttribute("product") Product product,
    							UpdateProductPayload payload,
                                Model model) {
        try {
        	this.productRestClient.updateProduct(product.id(), payload.title(), payload.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());
		} catch (BadRequestException exception) {
			model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "catalogue/products/edit_page";
		}
    }


	
	@PostMapping("delete")
	public String deleteProduct(@ModelAttribute("product") Product product) {
		this.productRestClient.deleteProduct(product.id());
		return "redirect:/catalogue/products/list";
	}
	
	@ExceptionHandler(NoSuchElementException.class)
	public String handleNoSuchElementException(NoSuchElementException e, Model model, HttpServletResponse response, Locale locale) {
		response.setStatus(HttpStatus.NOT_FOUND.value());
		model.addAttribute("error", this.messageSource.getMessage(e.getMessage(), new Object[0], e.getMessage(), locale));
		return "errors/404";
	}
	
}
