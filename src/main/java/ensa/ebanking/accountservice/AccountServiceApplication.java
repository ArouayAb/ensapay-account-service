package ensa.ebanking.accountservice;
import ensa.ebanking.accountservice.DTO.AdminProfileDTO;
import ensa.ebanking.accountservice.Services.AdminService;
import ensa.ebanking.accountservice.Services.CMIService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


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
