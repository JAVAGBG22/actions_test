package com.example.unitTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Retrieves all products from the repository.
     *
     * @return A list of all products.
     */
    //Se till att den hämtar alla produkter från repository.
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    /**
     * Creates a new product based on the provided ProductDTO.
     *
     * @param productDTO The data transfer object containing product details.
     * @return The saved product with an assigned ID.
     * @throws InvalidProductException if the product data is invalid.
     */
    public Product createProduct(ProductDTO productDTO) {
        // Validate product data
        if (productDTO.getPrice() < 0) {
            throw new InvalidProductException("Price cannot be negative.");
        }
        if (productDTO.getStockQuantity() < 0) {
            throw new InvalidProductException("Stock quantity cannot be negative.");
        }
        if (productDTO.getName() == null || productDTO.getName().trim().isEmpty()) {
            throw new InvalidProductException("Product name cannot be null or empty.");
        }
        // Add more validations as needed

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setColor(productDTO.getColor());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());

        return productRepository.save(product);
    }




    /**
     * Creates a new product based on the provided ProductDTO.
     *
     * @param productDTO The data transfer object containing product details.
     * @return The saved product with an assigned ID.
     */
    //Se till att när en giltig ProductDTO tillhandahålls, skapas och sparas en produkt korrekt.
    //Kontrollera att den returnerade produkten har ett tilldelat ID.
    /*public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setColor(productDTO.getColor());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());

        return productRepository.save(product);
    }
*/
    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     */
    //Se till att en produkt raderas med dess ID.
    //Verifiera att repository deleteById-metod anropas med rätt ID.
   public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     * @throws ProductNotFoundException if the product does not exist.
     */
   /* public void deleteProduct(String id) {
        // Check if the product exists
        boolean exists = productRepository.existsById(id);
        if (!exists) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        // Proceed to delete since it exists
        productRepository.deleteById(id);
    }*/

    /**
     * Retrieves products by their name.
     *
     * @param name The name of the products to retrieve.
     * @return A list of products matching the given name.
     */
    //Se till att den hämtar produkter som matchar det angivna namnet.
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    /**
     * Retrieves products within a specified price range.
     *
     * @param minPrice The minimum price.
     * @param maxPrice The maximum price.
     * @return A list of products within the price range.
     */
    //Se till att den hämtar produkter inom den angivna prisklassen.
    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    /**
     * Retrieves products by their color.
     *
     * @param color The color of the products to retrieve.
     * @return A list of products matching the given color.
     */
    //Se till att den hämtar produkter som matchar den givna färgen.
    public List<Product> getProductsByColor(String color) {
        return productRepository.findByColor(color);
    }

}
