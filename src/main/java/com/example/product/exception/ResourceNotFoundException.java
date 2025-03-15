package com.example.product.exception;

public class ResourceNotFoundException extends RuntimeException {
	public static final long serialVersionUID = 1l;
	
	public ResourceNotFoundException(String message) {
		super(message);
	}
}