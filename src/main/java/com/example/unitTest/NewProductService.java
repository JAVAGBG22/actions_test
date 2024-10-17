package com.example.unitTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class NewProductService {
    @Autowired
    private ProductRepository productRepository;

    /**
     * Retrieves all products from the repository.
     *
     * @return A list of all products.
     */
    public List<Product> getAllProductsN() {
        List<Product> products = productRepository.findAll();
        if (products == null) {
            throw new IllegalArgumentException("Failed to retrieve products.");
        }
        return products;
    }

    /**
     * Creates a new product based on the provided ProductDTO.
     *
     * @param productDTO The data transfer object containing product details.
     * @return The saved product with an assigned ID.
     * @throws IllegalArgumentException if required fields are missing or invalid.
     */
    public Product createProductN(ProductDTO productDTO) {
        // Validate required fields
        if (productDTO.getName() == null || productDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty.");
        }
        if (productDTO.getPrice() < 0) {
            throw new IllegalArgumentException("Product price cannot be negative.");
        }
        if (productDTO.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative.");
        }

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setColor(productDTO.getColor());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());

        return productRepository.save(product);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     * @throws NoSuchElementException if the product does not exist.
     * @throws IllegalArgumentException if the ID is null or empty.
     */
    public void deleteProductN(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty.");
        }

        boolean exists = productRepository.existsById(id);
        if (!exists) {
            throw new NoSuchElementException("Product not found with id: " + id);
        }

        productRepository.deleteById(id);
    }

    /**
     * Retrieves products by their name.
     *
     * @param name The name of the products to retrieve.
     * @return A list of products matching the given name.
     * @throws IllegalArgumentException if the name is null or empty.
     * @throws NoSuchElementException if no products match the given name.
     */
    public List<Product> getProductsByNameN(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty.");
        }

        List<Product> products = productRepository.findByName(name);
        if (products.isEmpty()) {
            throw new NoSuchElementException("No products found with name: " + name);
        }

        return products;
    }

    /**
     * Retrieves products within a specified price range.
     *
     * @param minPrice The minimum price.
     * @param maxPrice The maximum price.
     * @return A list of products within the price range.
     * @throws IllegalArgumentException if minPrice > maxPrice or any price is negative.
     */
    public List<Product> getProductsByPriceRangeN(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < 0) {
            throw new IllegalArgumentException("Price values cannot be negative.");
        }
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("minPrice cannot be greater than maxPrice.");
        }

        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
        if (products.isEmpty()) {
            throw new NoSuchElementException("No products found within price range: " + minPrice + " - " + maxPrice);
        }

        return products;
    }

    /**
     * Retrieves products by their color.
     *
     * @param color The color of the products to retrieve.
     * @return A list of products matching the given color.
     * @throws IllegalArgumentException if the color is null or empty.
     * @throws NoSuchElementException if no products match the given color.
     */
    public List<Product> getProductsByColorN(String color) {
        if (color == null || color.trim().isEmpty()) {
            throw new IllegalArgumentException("Product color cannot be null or empty.");
        }

        List<Product> products = productRepository.findByColor(color);
        if (products.isEmpty()) {
            throw new NoSuchElementException("No products found with color: " + color);
        }

        return products;
    }

    // ... other methods if any ...
}

