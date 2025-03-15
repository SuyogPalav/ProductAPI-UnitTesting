package com.example.product.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.product.payload.ProductDto;
import com.example.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        productDto = ProductDto.builder()
                .id(1)
                .name("Book")
                .price(1222.56)
                .description("This is the best book ever")
                .build();
    }

    @Test
    void testCreateProduct() throws Exception {
        when(productService.createProduct(any(ProductDto.class))).thenReturn(productDto);

        mockMvc.perform(post("/api/products/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(productDto)))
        		.andDo(print())			// It will print Request and Response in console
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Book"))
                .andExpect(jsonPath("$.price").value(1222.56));

        verify(productService, times(1)).createProduct(any(ProductDto.class));
    }

    @Test
    void testGetProductById() throws Exception {
        when(productService.getProductById(1)).thenReturn(productDto);

        mockMvc.perform(get("/api/products/1"))
				.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Book"));

        verify(productService, times(1)).getProductById(1);
    }

    @Test
    void testGetAllProducts() throws Exception {
        List<ProductDto> products = Arrays.asList(productDto);
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/products/all"))
				.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testUpdateProduct() throws Exception {
        when(productService.updateProduct(eq(1), any(ProductDto.class))).thenReturn(productDto);

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(productDto)))
				.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Book"))
                .andExpect(jsonPath("$.price").value(1222.56));

        verify(productService, times(1)).updateProduct(eq(1), any(ProductDto.class));
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1);

        mockMvc.perform(delete("/api/products/1"))
				.andDo(print())
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1);
    }

    @Test
    void testGetProductsByName() throws Exception {
        List<ProductDto> products = Arrays.asList(productDto);
        when(productService.getProductsByName("Book")).thenReturn(products);

        mockMvc.perform(get("/api/products/search?name=Book"))
				.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

        verify(productService, times(1)).getProductsByName("Book");
    }
}
