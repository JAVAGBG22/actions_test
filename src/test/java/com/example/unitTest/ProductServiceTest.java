package com.example.unitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    // Mock the ProductRepository to simulate database operations
    @Mock
    private ProductRepository productRepository;

    // Inject the mocks into ProductService
    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        // Initialize mocks created above
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the getAllProducts method to ensure it retrieves all products correctly.
     */
    @Test
    public void testGetAllProducts() {
        // Arrange: Create a list of sample products
        Product product1 = new Product();
        product1.setId("1");
        product1.setName("Product A");
        product1.setDescription("Description A");
        product1.setColor("Red");
        product1.setPrice(10.99);
        product1.setStockQuantity(100);

        Product product2 = new Product();
        product2.setId("2");
        product2.setName("Product B");
        product2.setDescription("Description B");
        product2.setColor("Blue");
        product2.setPrice(20.99);
        product2.setStockQuantity(200);

        List<Product> productList = Arrays.asList(product1, product2);

        // Mock the behavior of productRepository.findAll to return the product list
        when(productRepository.findAll()).thenReturn(productList);

        // Act: Call the getAllProducts method
        List<Product> result = productService.getAllProducts();

        // Assert: Verify that the returned list matches the mocked list
        assertNotNull(result, "The result should not be null");
        assertEquals(2, result.size(), "There should be two products in the list");
        assertEquals("Product A", result.get(0).getName(), "First product name should match");
        assertEquals("Product B", result.get(1).getName(), "Second product name should match");

        // Verify that productRepository.findAll was called once
        verify(productRepository, times(1)).findAll();
    }

    /**
     * Test the createProduct method to ensure it creates and saves a product correctly.
     */
    @Test
    public void testCreateProduct_Success() {
        // Arrange: Create a sample ProductDTO without an ID
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Product C");
        productDTO.setDescription("Description C");
        productDTO.setColor("Green");
        productDTO.setPrice(30.99);
        productDTO.setStockQuantity(300);

        // Create a Product object that would be saved (without ID)
        Product productToSave = new Product();
        productToSave.setName(productDTO.getName());
        productToSave.setDescription(productDTO.getDescription());
        productToSave.setColor(productDTO.getColor());
        productToSave.setPrice(productDTO.getPrice());
        productToSave.setStockQuantity(productDTO.getStockQuantity());

        // Create a Product object that represents the saved product (with ID)
        Product savedProduct = new Product();
        savedProduct.setId("3");
        savedProduct.setName(productDTO.getName());
        savedProduct.setDescription(productDTO.getDescription());
        savedProduct.setColor(productDTO.getColor());
        savedProduct.setPrice(productDTO.getPrice());
        savedProduct.setStockQuantity(productDTO.getStockQuantity());

        // Mock the behavior of productRepository.save to return the savedProduct
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act: Call the createProduct method
        Product result = productService.createProduct(productDTO);

        // Assert: Verify that the product was saved correctly
        assertNotNull(result.getId(), "Saved product should have an ID");
        assertEquals("Product C", result.getName(), "Product name should match");
        assertEquals("Description C", result.getDescription(), "Product description should match");
        assertEquals("Green", result.getColor(), "Product color should match");
        assertEquals(30.99, result.getPrice(), "Product price should match");
        assertEquals(300, result.getStockQuantity(), "Product stock quantity should match");

        // Verify that productRepository.save was called once with a Product object
        verify(productRepository, times(1)).save(any(Product.class));
    }

    /**
     * Test the deleteProduct method to ensure it deletes a product by ID.
     */
    @Test
    public void testDeleteProduct_Success() {
        // Arrange: Define a product ID to delete
        String productId = "1";

        // Mock the behavior of productRepository.deleteById (void method)
        doNothing().when(productRepository).deleteById(productId);

        // Act: Call the deleteProduct method
        productService.deleteProduct(productId);

        // Assert: Verify that deleteById was called once with the correct ID
        verify(productRepository, times(1)).deleteById(productId);
    }

    /**
     * Test the getProductsByName method to ensure it retrieves products by name.
     */
    @Test
    public void testGetProductsByName() {
        // Arrange: Define a product name and create sample products
        String productName = "Product A";
        Product product1 = new Product();
        product1.setId("1");
        product1.setName(productName);
        product1.setDescription("Description A");
        product1.setColor("Red");
        product1.setPrice(10.99);
        product1.setStockQuantity(100);

        List<Product> products = Arrays.asList(product1);

        // Mock the behavior of productRepository.findByName to return the products list
        when(productRepository.findByName(productName)).thenReturn(products);

        // Act: Call the getProductsByName method
        List<Product> result = productService.getProductsByName(productName);

        // Assert: Verify that the returned list matches the mocked list
        assertNotNull(result, "The result should not be null");
        assertEquals(1, result.size(), "There should be one product in the list");
        assertEquals(productName, result.get(0).getName(), "Product name should match");

        // Verify that findByName was called once with the correct name
        verify(productRepository, times(1)).findByName(productName);
    }

    /**
     * Test the getProductsByPriceRange method to ensure it retrieves products within a price range.
     */
    @Test
    public void testGetProductsByPriceRange() {
        // Arrange: Define price range and create sample products
        double minPrice = 10.00;
        double maxPrice = 30.00;

        Product product1 = new Product();
        product1.setId("1");
        product1.setName("Product A");
        product1.setDescription("Description A");
        product1.setColor("Red");
        product1.setPrice(15.99);
        product1.setStockQuantity(100);

        Product product2 = new Product();
        product2.setId("2");
        product2.setName("Product B");
        product2.setDescription("Description B");
        product2.setColor("Blue");
        product2.setPrice(25.99);
        product2.setStockQuantity(200);

        List<Product> products = Arrays.asList(product1, product2);

        // Mock the behavior of productRepository.findByPriceBetween to return the products list
        when(productRepository.findByPriceBetween(minPrice, maxPrice)).thenReturn(products);

        // Act: Call the getProductsByPriceRange method
        List<Product> result = productService.getProductsByPriceRange(minPrice, maxPrice);

        // Assert: Verify that the returned list matches the mocked list
        assertNotNull(result, "The result should not be null");
        assertEquals(2, result.size(), "There should be two products in the list");
        assertTrue(result.stream().allMatch(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice),
                "All products should be within the specified price range");

        // Verify that findByPriceBetween was called once with the correct parameters
        verify(productRepository, times(1)).findByPriceBetween(minPrice, maxPrice);
    }

    /**
     * Test the getProductsByColor method to ensure it retrieves products by color.
     */

    // KÖR TESTET INNAN ÄNDRAR DELETE I SERVICE KLASSEN
    @Test
    public void testGetProductsByColor() {
        // Arrange: Define a color and create sample products
        String color = "Red";

        Product product1 = new Product();
        product1.setId("1");
        product1.setName("Product A");
        product1.setDescription("Description A");
        product1.setColor(color);
        product1.setPrice(10.99);
        product1.setStockQuantity(100);

        Product product2 = new Product();
        product2.setId("2");
        product2.setName("Product B");
        product2.setDescription("Description B");
        product2.setColor(color);
        product2.setPrice(20.99);
        product2.setStockQuantity(200);

        List<Product> products = Arrays.asList(product1, product2);

        // Mock the behavior of productRepository.findByColor to return the products list
        when(productRepository.findByColor(color)).thenReturn(products);

        // Act: Call the getProductsByColor method
        List<Product> result = productService.getProductsByColor(color);

        // Assert: Verify that the returned list matches the mocked list
        assertNotNull(result, "The result should not be null");
        assertEquals(2, result.size(), "There should be two products in the list");
        assertTrue(result.stream().allMatch(p -> p.getColor().equals(color)),
                "All products should have the specified color");

        // Verify that findByColor was called once with the correct color
        verify(productRepository, times(1)).findByColor(color);
    }


}

