package com.example.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	List<Product> findByName(String prd);
}