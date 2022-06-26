package ensa.ebanking.accountservice;

import ensa.ebanking.accountservice.DAO.ClientProfileDAO;
import ensa.ebanking.accountservice.DAO.CreanceDAO;
import ensa.ebanking.accountservice.DAO.CreancierDAO;
import ensa.ebanking.accountservice.DAO.ServiceProviderDAO;
import ensa.ebanking.accountservice.DTO.AdminProfileDTO;
import ensa.ebanking.accountservice.DTO.AgentProfileDTO;
import ensa.ebanking.accountservice.DTO.ClientProfileDTO;
import ensa.ebanking.accountservice.Entities.Creance;
import ensa.ebanking.accountservice.Enums.CreanceStatus;
import ensa.ebanking.accountservice.Enums.ProductType;
import ensa.ebanking.accountservice.Helpers.BankAccountHelper;
import ensa.ebanking.accountservice.Services.AdminService;
import ensa.ebanking.accountservice.Services.AgentService;
import ensa.ebanking.accountservice.Services.CMIService;
import ensa.ebanking.accountservice.Services.ClientService;
import ensa.ebanking.accountservice.Helpers.EmailHelper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
@EnableScheduling
public class AccountServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(AccountServiceApplication.class, args);

	}

	@Bean
	CommandLineRunner run(AdminService adminService, CMIService cmiService) {
		return args -> {

			adminService.registerAdmin(
					new AdminProfileDTO(
							"Admin",
							"0624916360",
							"main-admin@ensapay.com"
					),
					"ensapay@@2022"
			);

			cmiService.saveStaticDataToDB();

		};
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
