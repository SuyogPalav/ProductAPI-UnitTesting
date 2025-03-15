package com.example.product.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.product.entity.Product;
import com.example.product.payload.ProductDto;
import com.example.product.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        product = new Product(1, "Mercedes", 98765432.0, "This is best car ever");
        productDto = new ProductDto(1, "Mercedes", 98765432.0, "This is best book ever");
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto savedProduct = productService.createProduct(productDto);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("Mercedes");
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        ProductDto foundProduct = productService.getProductById(1);

        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getPrice()).isEqualTo(product.getPrice());
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        List<ProductDto> products = productService.getAllProducts();

        assertThat(products).isNotEmpty();
        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    void testUpdateProduct() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto updatedProduct = productService.updateProduct(1, productDto);

        assertThat(updatedProduct).isNotNull();
        assertThat(updatedProduct.getName()).isEqualTo(product.getName());
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(any(Product.class));

        productService.deleteProduct(1);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void testGetProductsByName() {
        when(productRepository.findByName("Mercedes")).thenReturn(Arrays.asList(product));

        List<ProductDto> products = productService.getProductsByName("Mercedes");

        assertThat(products).isNotEmpty();
        assertThat(products.get(0).getName()).isEqualTo(product.getName());
    }
}


/*

Question: when(productRepository.save(any(Product.class))).thenReturn(product); What Does This Do? 

Answer:
- It tells Mockito: "Whenever productRepository.save() is called with any Product object, return product."
- This is not an assignment but a way to override the behavior of save() in a mock repository.

---

Question: Why Are We Using It?

Answer:
- In a unit test, we don't actually save data to a database.
- productRepository.save() would normally interact with a real database, but since we're using Mockito, it just returns the predefined product.
- When productService.createProduct(productDto) calls productRepository.save(), it gets back product instead of actually inserting it into a database.

---

Question: How Is This Used?

Answer:
- productService.createProduct(productDto) calls productRepository.save(product).
- Because we mocked save(), it returns product instead of actually saving.
- Then, we assert that the returned ProductDto has the expected values.

---

Question: Is It Necessary?

Answer:
- Yes! Without this, productRepository.save() would return null (default for an unmocked method), causing productService.createProduct(productDto) to fail.

*/


// See below, another explanation to understand more


/*

Question: when(productRepository.findAll()).thenReturn(Arrays.asList(product)); What Does This Do?

Answer:
- It tells Mockito: "Whenever productRepository.findAll() is called, return a list containing product instead of actually fetching data from a database."


Question: Why Are We Using It?

Answer:
- Normally, productRepository.findAll() would query a real database and return all products.
- But in unit testing, we don't use a real database.
- So, we mock the behavior by making findAll() return a predefined list.

Question: How Is This Used in the Test?

- productService.getAllProducts() calls productRepository.findAll()
- Since findAll() is mocked, it returns [product] instead of making a real database call.
- The returned list (products) now contains one product
- We assert that it's not empty and has exactly one element.

*/