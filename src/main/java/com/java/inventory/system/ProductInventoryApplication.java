package com.java.inventory.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Product Inventory Spring Boot application.
 * This class contains the main method, which launches the Spring Boot application.
 */
@SpringBootApplication
public class ProductInventoryApplication {

	/**
	 * The main method that runs the Spring Boot application.
	 *
	 * @param args command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(ProductInventoryApplication.class, args);
	}

}
