package com.java.inventory.system.controller;

import com.java.inventory.system.model.Product;
import com.java.inventory.system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller class that provides endpoints for managing products in the inventory system.
 * It exposes CRUD operations for {@link Product} entities.
 */
@RestController
@RequestMapping("/v1/product")
public class ProductController {

    private final ProductService productService;

    /**
     * Constructs a new {@link ProductController} with the given {@link ProductService}.
     *
     * @param productService the service for handling product operations.
     */
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Endpoint to retrieve all products.
     *
     * @return a list of all products.
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Endpoint to retrieve a product by its ID.
     *
     * @param id the ID of the product to retrieve.
     * @return a ResponseEntity containing the product, or a 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Endpoint to create a new product.
     *
     * @param request the product data to create.
     * @return a ResponseEntity containing the created product, with HTTP status 201.
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product request) {
        Product createdProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    /**
     * Endpoint to update an existing product.
     *
     * @param id      the ID of the product to update.
     * @param request the updated product data.
     * @return a ResponseEntity containing the updated product, or a 404 if the product is not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product request) {
        Product updatedProduct = productService.updateProduct(id, request);
        return updatedProduct != null ? ResponseEntity.ok(updatedProduct) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Endpoint to delete a product by its ID.
     *
     * @param id the ID of the product to delete.
     * @return a ResponseEntity with HTTP status 204 (No Content) if the product was deleted,
     *         or a 404 if the product was not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id) ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

