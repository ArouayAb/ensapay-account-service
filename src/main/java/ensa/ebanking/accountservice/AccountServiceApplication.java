package ensa.ebanking.accountservice;

import ensa.ebanking.accountservice.DTO.ClientProfileDTO;
import ensa.ebanking.accountservice.Enums.ProductType;
import ensa.ebanking.accountservice.Services.ClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner run(ClientService clientService) {
		return args -> {
			clientService.registerClient(new ClientProfileDTO(ProductType.HSSAB1, "name1", "surname1", "0111222333", "email1@email.com"));
		};
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
