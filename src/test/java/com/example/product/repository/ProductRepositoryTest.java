package com.example.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.product.entity.Product;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	Product product1;

	@BeforeEach
	void setUp() throws Exception {
		product1 = Product.builder().name("Book").price(1222.56).description("This is best book ever").build();
		productRepository.save(product1);
	}

	@AfterEach
	void tearDown() throws Exception {
		product1 = null;
		productRepository.deleteAll();

	}

	@Test
	void testFindByProductName() {
		List<Product> products = productRepository.findByName("Book");
		assertThat(products).isNotEmpty();
		assertThat(products.get(0).getName()).isEqualTo(product1.getName());
	}

	@Test
	void testFindByProductNameWhichIsNotPresent() {
		List<Product> products = productRepository.findByName("Clock");
		assertThat(products.isEmpty()).isTrue();
//		assertThat(products).isNotEmpty();
//		assertThat(products.get(0).getName()).isEqualTo("Book");
	}

}
