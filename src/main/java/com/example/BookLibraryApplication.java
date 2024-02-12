package com.example;

import com.example.aspect.TestClassAOP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.FileNotFoundException;

@Slf4j
@SpringBootApplication
public class BookLibraryApplication {

	public static void main(String[] args) throws InterruptedException, FileNotFoundException {

//		ConfigurableApplicationContext context =
		SpringApplication.run(BookLibraryApplication.class, args);
//		TestClassAOP testClassAOP = context.getBean(TestClassAOP.class);
//
//		String result = testClassAOP.method1("AaA");
//		log.error(result);


//		testClassAOP.method4();
//		testClassAOP.method2();
//		testClassAOP.method3();

	}
}