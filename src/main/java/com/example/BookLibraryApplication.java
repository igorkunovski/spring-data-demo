package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BookLibraryApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(BookLibraryApplication.class, args);

	}
}
