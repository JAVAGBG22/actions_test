package com.example.unitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    // Rensar databasen innan varje test så att den börjar från en tom bas
    @BeforeEach
    void cleanDatabase() {
        productRepository.deleteAll();
    }

    // Test för att skapa en produkt (POST /api/products)
    @Test
    public void testCreateProduct() throws Exception {
        // JSON för en ny produkt
        String productJson = """
            {
              "name": "Test Product",
              "description": "A test product",
              "price": 99.99,
              "stockQuantity": 50
            }
            """;

        // Skicka en POST-request till controllern
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated()); // Verifierar att svaret är 201 Created

        // Kontrollera att produkten sparats i databasen
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("Test Product");
    }

    // Test för att hämta alla produkter (GET /api/products)
    @Test
    public void testGetAllProducts() throws Exception {
        // Spara några produkter i databasen
        Product product1 = new Product();
        product1.setName("Product A");
        product1.setDescription("Description A");
        product1.setPrice(10.99);
        product1.setStockQuantity(100);

        Product product2 = new Product();
        product2.setName("Product B");
        product2.setDescription("Description B");
        product2.setPrice(20.99);
        product2.setStockQuantity(200);

        productRepository.save(product1);
        productRepository.save(product2);

        // Skicka en GET-request och verifiera svaret
        MvcResult result = mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verifierar att svaret är 200 OK
                .andReturn();

        // Kontrollera att svaret innehåller båda produkterna
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(jsonResponse).contains("Product A", "Product B");
    }

    // Test för att hämta produkter efter namn (GET /api/products/name/{name})
    @Test
    public void testGetProductsByName() throws Exception {
        // Spara en produkt i databasen
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Description for Test Product");
        product.setPrice(99.99);
        product.setStockQuantity(50);

        productRepository.save(product);

        // Skicka en GET-request för att hämta produkten baserat på namn
        MvcResult result = mockMvc.perform(get("/api/products/name/Test Product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verifierar att svaret är 200 OK
                .andReturn();

        // Kontrollera att svaret innehåller produkten
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(jsonResponse).contains("Test Product");
    }

    // Test för att hämta produkter inom ett prisintervall (GET /api/products/price)
    @Test
    public void testGetProductsByPriceRange() throws Exception {
        // Spara produkter med olika priser
        Product product1 = new Product();
        product1.setName("Product A");
        product1.setDescription("Description A");
        product1.setPrice(15.99);
        product1.setStockQuantity(100);

        Product product2 = new Product();
        product2.setName("Product B");
        product2.setDescription("Description B");
        product2.setPrice(25.99);
        product2.setStockQuantity(200);

        productRepository.save(product1);
        productRepository.save(product2);

        // Skicka en GET-request för att hämta produkter inom prisintervallet
        MvcResult result = mockMvc.perform(get("/api/products/price")
                        .param("minPrice", "10.0")
                        .param("maxPrice", "30.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verifierar att svaret är 200 OK
                .andReturn();

        // Kontrollera att svaret innehåller båda produkterna
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(jsonResponse).contains("Product A", "Product B");
    }

    // Test för att hämta produkter efter färg (GET /api/products/color/{color})
    @Test
    public void testGetProductsByColor() throws Exception {
        // Spara produkter med samma färg
        Product product1 = new Product();
        product1.setName("Product A");
        product1.setDescription("Description A");
        product1.setColor("Red");
        product1.setPrice(15.99);
        product1.setStockQuantity(100);

        Product product2 = new Product();
        product2.setName("Product B");
        product2.setDescription("Description B");
        product2.setColor("Red");
        product2.setPrice(25.99);
        product2.setStockQuantity(200);

        productRepository.save(product1);
        productRepository.save(product2);

        // Skicka en GET-request för att hämta produkterna baserat på färg
        MvcResult result = mockMvc.perform(get("/api/products/color/Red")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verifierar att svaret är 200 OK
                .andReturn();

        // Kontrollera att svaret innehåller båda produkterna
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(jsonResponse).contains("Product A", "Product B");
    }

    // Test för att radera en produkt (DELETE /api/products/{id})
    @Test
    public void testDeleteProduct() throws Exception {
        // Spara en produkt i databasen
        Product product = new Product();
        product.setName("Product to Delete");
        product.setDescription("Will be deleted");
        product.setPrice(9.99);
        product.setStockQuantity(50);

        Product savedProduct = productRepository.save(product);

        // Skicka en DELETE-request för att ta bort produkten
        mockMvc.perform(delete("/api/products/" + savedProduct.getId()))
                .andExpect(status().isNoContent()); // Verifierar att svaret är 204 No Content

        // Kontrollera att produkten har tagits bort från databasen
        assertThat(productRepository.findById(savedProduct.getId())).isEmpty();
    }
}
