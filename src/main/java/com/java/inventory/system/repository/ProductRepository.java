package com.java.inventory.system.repository;


import com.java.inventory.system.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing {@link Product} entities from the database.
 * Extends {@link JpaRepository} to provide CRUD operations and more for {@link Product} entities.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // No additional methods are needed as JpaRepository already provides common operations like
    // save(), findById(), findAll(), deleteById(), etc.
}
