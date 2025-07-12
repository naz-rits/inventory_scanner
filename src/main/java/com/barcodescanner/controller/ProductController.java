package com.barcodescanner.controller;

import com.barcodescanner.model.Product;
import com.barcodescanner.repository.ProductRepository;
import com.barcodescanner.services.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{barcode}")
    public Optional<Product> getProduct(@PathVariable String barcode) {
        return productService.findByBarcode(barcode);
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.save(product);
    }

    @PutMapping("/{barcode}")
    public Product updateProduct(@PathVariable String barcode, @RequestBody Product product) {
        product.setBarcode(barcode);
        return productService.save(product);
    }
}
