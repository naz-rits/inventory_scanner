package com.barcodescanner.controller;

import com.barcodescanner.model.Product;
import com.barcodescanner.repository.ProductRepository;
import com.barcodescanner.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product saved = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{barcode}")
    public Product updateProduct(@PathVariable String barcode, @RequestBody Product product) {
        product.setBarcode(barcode);
        return productService.save(product);
    }

    @DeleteMapping("/{barcode}")
    public void deleteProduct(@PathVariable String barcode) {
        Optional<Product> product = productService.findByBarcode(barcode);
        product.ifPresentOrElse(product1 -> {
            productService.deleteById(product1.getId());
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        });
    }
}
