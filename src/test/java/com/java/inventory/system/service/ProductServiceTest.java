package com.java.inventory.system.service;

import com.java.inventory.system.model.Product;
import com.java.inventory.system.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    public void setup() {
        product = new Product(1L, "Laptop", "High-performance laptop", "EL", 10, 999.99);
    }

    @Test
    public void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(product);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals("Laptop", result.get().getName());
    }

    @Test
    public void testUpdateProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = productService.updateProduct(1L, product);

        assertNotNull(updatedProduct);
        assertEquals("Laptop", updatedProduct.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testDeleteProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);

        boolean isDeleted = productService.deleteProduct(1L);

        assertTrue(isDeleted);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteProduct_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        boolean isDeleted = productService.deleteProduct(1L);

        assertFalse(isDeleted);
        verify(productRepository, times(0)).deleteById(1L);
    }
}
