package com.sepm.authbased;

import com.sepm.authbased.modules.user.Role;
import com.sepm.authbased.modules.user.User;
import com.sepm.authbased.modules.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthBasedApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthBasedApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			createUserRandom(userRepository, passwordEncoder);
		};
	};

	private void createUserRandom(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		String firstName = "Admin";
		String lastName = "Nguyen";
		String email = "admin@gmail.com";
		User user = new User(
				firstName,
				lastName,
				email,
				passwordEncoder.encode("123456"),
				Role.ADMIN);

		userRepository.save(user);
		System.out.println(email);
	}
}
