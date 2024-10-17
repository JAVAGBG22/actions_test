package com.example.unitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ProductServiceNegativeTest {
    // Mock the ProductRepository to simulate database operations
    @Mock
    private ProductRepository productRepository;

    // Inject the mocks into ProductService
    @InjectMocks
    private NewProductService productService;

    @BeforeEach
    public void setUp() {
        // Initialize mocks created above
        MockitoAnnotations.openMocks(this);
    }



    /**
     * Test the deleteProduct method to ensure it throws an exception when deleting a non-existent product.
     */
    @Test
    public void testDeleteProduct_ProductDoesNotExist() {
        // Arrange
        String nonExistentProductId = "nonexistent_id";

        // Mock the behavior: Simulate that the product does not exist
        // When checking existence, return false
        when(productRepository.existsById(nonExistentProductId)).thenReturn(false);

        // Act & Assert: Expect ProductNotFoundException when deleting a non-existent product
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProductN(nonExistentProductId);
        }, "Expected deleteProduct to throw ProductNotFoundException, but it didn't");

        // Verify the exception message
        assertTrue(exception.getMessage().contains("Product not found with id: " + nonExistentProductId));

        // Verify that deleteById was never called since the product does not exist
        verify(productRepository, never()).deleteById(anyString());
    }

    // TESTET FAILAR
    // Testet testDeleteProduct_ProductDoesNotExist misslyckades eftersom
    // deleteProduct-metoden inte gav den förväntade ProductNotFoundException.
    // Detta illustrerar att den nuvarande implementeringen inte hanterar scenariot
    // där en produkt som ska raderas inte existerar.Testet testDeleteProduct_ProductDoesNotExist
    // misslyckades eftersom deleteProduct-metoden inte gav den förväntade ProductNotFoundException.
    // Detta illustrerar att den nuvarande implementeringen inte hanterar scenariot där en
    // produkt som ska raderas inte existerar.

    // Steg-för-steg
    //Ändra ProductService.deleteProduct: Kontrollera om produkten finns innan
    // du försöker ta bort den. Om det inte gör det, kasta ProductNotFoundException.
    //Uppdatera testet: Se till att testet överensstämmer med det nya beteendet.

    // Ändra ProductService.deleteProduct
    //Uppdatera metoden deleteProduct för att inkludera existenskontroll och kasta ett
    // undantag om produkten inte finns.



    /**
     * Negative Test: getAllProducts returns null (simulate repository failure).
     */
    @Test
    public void testGetAllProducts_NullResponse() {
        // Arrange
        when(productRepository.findAll()).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.getAllProductsN();
        });

        assertEquals("Failed to retrieve products.", exception.getMessage());

        // Verify
        verify(productRepository, times(1)).findAll();
    }

    /**
     * Negative Test: createProduct with negative price should throw IllegalArgumentException.
     */
    @Test
    public void testCreateProduct_NegativePrice() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setDescription("Test Description");
        productDTO.setColor("Blue");
        productDTO.setPrice(-20.0); // Invalid price
        productDTO.setStockQuantity(10);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.createProductN(productDTO);
        });

        assertEquals("Product price cannot be negative.", exception.getMessage());

        // Verify
        verify(productRepository, never()).save(any(Product.class));
    }

    /**
     * Negative Test: createProduct with missing name should throw IllegalArgumentException.
     */
    @Test
    public void testCreateProduct_MissingName() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setDescription("No name product");
        productDTO.setColor("Red");
        productDTO.setPrice(15.0);
        productDTO.setStockQuantity(5);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.createProductN(productDTO);
        });

        assertEquals("Product name cannot be null or empty.", exception.getMessage());

        // Verify
        verify(productRepository, never()).save(any(Product.class));
    }

    /**
     * Negative Test: deleteProduct with non-existent ID should throw NoSuchElementException.
     */
    /*@Test
    public void testDeleteProduct_NonExistentId() {
        // Arrange
        String nonExistentId = "nonexistent123";

        when(productRepository.existsById(nonExistentId)).thenReturn(false);

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            productService.deleteProduct(nonExistentId);
        });

        assertEquals("Product not found with id: " + nonExistentId, exception.getMessage());

        // Verify
        verify(productRepository, times(1)).existsById(nonExistentId);
        verify(productRepository, never()).deleteById(anyString());
    }*/

    /**
     * Negative Test: deleteProduct with null ID should throw IllegalArgumentException.
     */
    /*@Test
    public void testDeleteProduct_NullId() {
        // Arrange
        String productId = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.deleteProduct(productId);
        });

        assertEquals("Product ID cannot be null or empty.", exception.getMessage());

        // Verify
        verify(productRepository, never()).existsById(anyString());
        verify(productRepository, never()).deleteById(anyString());
    }
*/
    /**
     * Negative Test: getProductsByName with non-existent name should throw NoSuchElementException.
     */
    @Test
    public void testGetProductsByName_NoMatches() {
        // Arrange
        String nonExistentName = "UnknownProduct";

        when(productRepository.findByName(nonExistentName)).thenReturn(Collections.emptyList());

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            productService.getProductsByNameN(nonExistentName);
        });

        assertEquals("No products found with name: " + nonExistentName, exception.getMessage());

        // Verify
        verify(productRepository, times(1)).findByName(nonExistentName);
    }

    /**
     * Negative Test: getProductsByName with null name should throw IllegalArgumentException.
     */
    @Test
    public void testGetProductsByName_NullName() {
        // Arrange
        String name = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductsByNameN(name);
        });

        assertEquals("Product name cannot be null or empty.", exception.getMessage());

        // Verify
        verify(productRepository, never()).findByName(anyString());
    }

    /**
     * Negative Test: getProductsByPriceRange with minPrice > maxPrice should throw IllegalArgumentException.
     */
    @Test
    public void testGetProductsByPriceRange_InvalidRange() {
        // Arrange
        double minPrice = 100.0;
        double maxPrice = 50.0;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductsByPriceRangeN(minPrice, maxPrice);
        });

        assertEquals("minPrice cannot be greater than maxPrice.", exception.getMessage());

        // Verify
        verify(productRepository, never()).findByPriceBetween(anyDouble(), anyDouble());
    }

    /**
     * Negative Test: getProductsByPriceRange with negative minPrice should throw IllegalArgumentException.
     */
    @Test
    public void testGetProductsByPriceRange_NegativeMinPrice() {
        // Arrange
        double minPrice = -10.0;
        double maxPrice = 50.0;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductsByPriceRangeN(minPrice, maxPrice);
        });

        assertEquals("Price values cannot be negative.", exception.getMessage());

        // Verify
        verify(productRepository, never()).findByPriceBetween(anyDouble(), anyDouble());
    }

    /**
     * Negative Test: getProductsByPriceRange with no products in range should throw NoSuchElementException.
     */
    @Test
    public void testGetProductsByPriceRange_NoMatches() {
        // Arrange
        double minPrice = 10.0;
        double maxPrice = 20.0;

        when(productRepository.findByPriceBetween(minPrice, maxPrice)).thenReturn(Collections.emptyList());

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            productService.getProductsByPriceRangeN(minPrice, maxPrice);
        });

        assertEquals("No products found within price range: " + minPrice + " - " + maxPrice, exception.getMessage());

        // Verify
        verify(productRepository, times(1)).findByPriceBetween(minPrice, maxPrice);
    }

    /**
     * Negative Test: getProductsByColor with non-existent color should throw NoSuchElementException.
     */
    @Test
    public void testGetProductsByColor_NoMatches() {
        // Arrange
        String color = "InvisibleColor";

        when(productRepository.findByColor(color)).thenReturn(Collections.emptyList());

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            productService.getProductsByColorN(color);
        });

        assertEquals("No products found with color: " + color, exception.getMessage());

        // Verify
        verify(productRepository, times(1)).findByColor(color);
    }

    /**
     * Negative Test: getProductsByColor with null color should throw IllegalArgumentException.
     */
    @Test
    public void testGetProductsByColor_NullColor() {
        // Arrange
        String color = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductsByColorN(color);
        });

        assertEquals("Product color cannot be null or empty.", exception.getMessage());

        // Verify
        verify(productRepository, never()).findByColor(anyString());
    }

    /**
     * Negative Test: getProductsByColor with empty color should throw IllegalArgumentException.
     */
    @Test
    public void testGetProductsByColor_EmptyColor() {
        // Arrange
        String color = "   ";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductsByColorN(color);
        });

        assertEquals("Product color cannot be null or empty.", exception.getMessage());

        // Verify
        verify(productRepository, never()).findByColor(anyString());
    }




}
