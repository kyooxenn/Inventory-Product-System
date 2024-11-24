package com.java.inventory.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.inventory.system.model.Product;
import com.java.inventory.system.service.ProductService;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private ProductService productService;

    private static Product product;

    private static MockWebServer mockWebServer;

    @BeforeAll
    public static void setup() throws IOException {
        product = new Product(1L, "Laptop", "High-performance laptop", "EL", 10, 999.99);
        mockWebServer = new MockWebServer();
        mockWebServer.start(8099);
    }

    @AfterAll
    static void tearDown() throws IOException {
        Mockito.clearAllCaches();
        mockWebServer.shutdown();
    }

    @Test
    @Sql("/testData/V1.0__create_product_test_data.sql")
    public void testCreateProduct() throws Exception {
        // Perform POST request
        mockMvc.perform(post("/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())  // Expecting HTTP status 201 (Created)
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.productType").value("EL"));
    }

    @Test
    public void testGetAllProducts() throws Exception {
        // Perform GET request
        mockMvc.perform(get("/v1/product"))
                .andDo(print())
                .andExpect(status().isOk())  // Expecting HTTP status 200 (OK)
                .andExpect(jsonPath("$[0].name").value("Desktop"))
                .andExpect(jsonPath("$[0].productType").value("EL"));
    }

    @Test
    public void testGetProductById() throws Exception {
        // Mock service layer method
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        // Perform GET request for a single product by ID
        mockMvc.perform(get("/v1/product/{id}", 1L))
                .andExpect(status().isOk())  // Expecting HTTP status 200 (OK)
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    public void testGetProductById_NotFound() throws Exception {
        // Mock service layer method to return empty
        when(productService.getProductById(10L)).thenReturn(Optional.empty());

        // Perform GET request for a non-existent product
        mockMvc.perform(get("/v1/product/{id}", 10L))
                .andExpect(status().isNotFound());  // Expecting HTTP status 404 (Not Found)
    }

    @Test
    public void testUpdateProduct() throws Exception {
        // Mock service layer method
        when(productService.updateProduct(1L, product)).thenReturn(product);

        // Perform PUT request to update the product
        mockMvc.perform(put("/v1/product/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())  // Expecting HTTP status 200 (OK)
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    public void testUpdateProduct_NotFound() throws Exception {
        // Mock service layer method to return null if the product doesn't exist
        when(productService.updateProduct(3L, product)).thenReturn(null);

        // Perform PUT request to update a non-existent product
        mockMvc.perform(put("/v1/product/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isNotFound());  // Expecting HTTP status 404 (Not Found)
    }


    @Test
    public void testDeleteProduct() throws Exception {
        // Mock service layer method
        when(productService.deleteProduct(1L)).thenReturn(true);

        // Perform DELETE request
        mockMvc.perform(delete("/v1/product/{id}", 1L))
                .andExpect(status().isNoContent());  // Expecting HTTP status 204 (No Content)
    }

    @Test
    public void testDeleteProduct_NotFound() throws Exception {
        // Mock service layer method
        when(productService.deleteProduct(10L)).thenReturn(false);

        // Perform DELETE request for a non-existent product
        mockMvc.perform(delete("/v1/product/{id}", 10L))
                .andExpect(status().isNotFound());  // Expecting HTTP status 404 (Not Found)
    }
}
