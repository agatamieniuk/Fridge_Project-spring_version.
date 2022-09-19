package com.infoshareacademy.controller;

import com.infoshareacademy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

}