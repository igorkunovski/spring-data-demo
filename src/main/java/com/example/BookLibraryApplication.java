package com.example;

import com.example.repository.UserRepository;
import com.example.secutity.Role;
import com.example.secutity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BookLibraryApplication {

	public static void main(String[] args) {
		UserRepository userRepository = SpringApplication.run(BookLibraryApplication.class, args).getBean(UserRepository.class);

		userRepository.save(new User("admin", "admin", Role.ADMIN));
		userRepository.save(new User("reader", "reader", Role.READER));
		userRepository.save(new User("dev", "dev", Role.DEVELOPER));
	}
}
