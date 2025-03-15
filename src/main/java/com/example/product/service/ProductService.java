package com.example.product.service;


import java.util.List;

import com.example.product.payload.ProductDto;

public interface ProductService {
    ProductDto createProduct(ProductDto productDTO);
    ProductDto getProductById(Integer id);
    List<ProductDto> getAllProducts();
    ProductDto updateProduct(Integer id, ProductDto productDTO);
    void deleteProduct(Integer id);
    List<ProductDto> getProductsByName(String name);

}