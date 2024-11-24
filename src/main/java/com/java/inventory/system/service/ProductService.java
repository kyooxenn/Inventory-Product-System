package com.java.inventory.system.service;

import com.java.inventory.system.model.Product;
import com.java.inventory.system.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class that provides business logic for managing products in the inventory system.
 * It interacts with the {@link ProductRepository} to perform CRUD operations on {@link Product} entities.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Constructs a new {@link ProductService} with the given {@link ProductRepository}.
     *
     * @param productRepository the repository for accessing product data.
     */
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    /**
     * Retrieves a list of all products in the inventory.
     *
     * @return a list of {@link Product} entities.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id the ID of the product to retrieve.
     * @return an {@link Optional} containing the {@link Product} if found, otherwise empty.
     */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Creates a new product in the inventory.
     *
     * @param request the {@link Product} to be created.
     * @return the created {@link Product}.
     */
    public Product createProduct(Product request) {
        return productRepository.save(request);
    }

    /**
     * Updates an existing product with new data.
     *
     * @param id the ID of the product to update.
     * @param request the {@link Product} containing the updated data.
     * @return the updated {@link Product}, or {@code null} if the product with the specified ID does not exist.
     */
    public Product updateProduct(Long id, Product request) {

        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            // Retrieve the existing product from the repository
            Product productToUpdate = existingProduct.get();

            // Update the fields of the existing product
            productToUpdate.setName(request.getName());
            productToUpdate.setDescription(request.getDescription());
            productToUpdate.setProductType(request.getProductType());
            productToUpdate.setQuantity(request.getQuantity());
            productToUpdate.setUnitPrice(request.getUnitPrice());

            // Save the updated product back to the repository
            return productRepository.save(productToUpdate);  // This will update the existing entity
        }
        return null;  // Product not found
    }


    /**
     * Deletes a product from the inventory.
     *
     * @param id the ID of the product to delete.
     * @return {@code true} if the product was successfully deleted, {@code false} if the product with the specified ID does not exist.
     */
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
