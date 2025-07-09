package com.barcodescanner.repository;

import com.barcodescanner.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByBarcode(String barcode);
}
