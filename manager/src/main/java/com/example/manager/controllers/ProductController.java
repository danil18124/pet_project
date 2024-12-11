package com.example.manager.controllers;

import java.util.Locale;
import java.util.NoSuchElementException;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.manager.entities.Product;
import com.example.manager.payload.UpdateProductPayload;
import com.example.manager.services.ProductService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products/{productId:\\d+}")
public class ProductController {
	
	private final ProductService productService;
	private final MessageSource messageSource;
	
	@ModelAttribute("product")
	Product product(@PathVariable("productId") Integer id, Model model) {
		return this.productService.findProduct(id).orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
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
    public String updateProduct(@ModelAttribute(name = "product", binding = false) Product product,
                                @Valid UpdateProductPayload payload,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return "catalogue/products/edit_page";
        } else {
            this.productService.updateProduct(product.getId(), payload.title(), payload.details());
            return "redirect:/catalogue/products/%d".formatted(product.getId());
        }
    }


	
	@PostMapping("delete")
	public String deleteProduct(@ModelAttribute("product") Product product) {
		this.productService.deleteProduct(product.getId());
		return "redirect:/catalogue/products/list";
	}
	
	@ExceptionHandler(NoSuchElementException.class)
	public String handleNoSuchElementException(NoSuchElementException e, Model model, HttpServletResponse response, Locale locale) {
		response.setStatus(HttpStatus.NOT_FOUND.value());
		model.addAttribute("error", this.messageSource.getMessage(e.getMessage(), new Object[0], e.getMessage(), locale));
		return "errors/404";
	}
	
}
