package com.example.product.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.product.entity.Product;
import com.example.product.exception.ResourceNotFoundException;
import com.example.product.payload.ProductDto;
import com.example.product.repository.ProductRepository;
import com.example.product.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;

	@Override
	public ProductDto createProduct(ProductDto productDTO) {
		Product product = new Product(null, productDTO.getName(), productDTO.getPrice(), productDTO.getDescription());
		product = productRepository.save(product);
		return new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getDescription());
	}

	@Override
	public ProductDto getProductById(Integer id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
		return new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getDescription());
	}

	@Override
	public List<ProductDto> getAllProducts() {
		return productRepository.findAll().stream().map(product -> new ProductDto(product.getId(), product.getName(),
				product.getPrice(), product.getDescription())).collect(Collectors.toList());
	}

	@Override
	public ProductDto updateProduct(Integer id, ProductDto productDTO) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
		product.setName(productDTO.getName());
		product.setPrice(productDTO.getPrice());
		product.setDescription(productDTO.getDescription());
		productRepository.save(product);
		return new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getDescription());
	}

	@Override
	public void deleteProduct(Integer id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
		productRepository.delete(product);
	}

	@Override
	public List<ProductDto> getProductsByName(String name) {
		List<ProductDto> productDto = productRepository.findByName(name).stream()
				.map(product -> new ProductDto(product.getId(), product.getName(), product.getPrice(),
						product.getDescription()))
				.collect(Collectors.toList());
		
		System.out.println(productDto);

		if (productDto.isEmpty()) {
			new ResourceNotFoundException("Product not found with name: " + name);
		}
		return productDto;
	}

}