package ensa.ebanking.accountservice;

import ensa.ebanking.accountservice.DTO.AdminProfileDTO;
import ensa.ebanking.accountservice.DTO.AgentProfileDTO;
import ensa.ebanking.accountservice.DTO.ClientProfileDTO;
import ensa.ebanking.accountservice.Enums.ProductType;
import ensa.ebanking.accountservice.Services.AdminService;
import ensa.ebanking.accountservice.Services.AgentService;
import ensa.ebanking.accountservice.Services.ClientService;
import ensa.ebanking.accountservice.Services.EmailSenderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.time.LocalDate;

@SpringBootApplication
public class AccountServiceApplication {


	public static void main(String[] args) {



		SpringApplication.run(AccountServiceApplication.class, args
		);

	}

	@Bean
	CommandLineRunner run(ClientService clientService, AgentService agentService, AdminService adminService,EmailSenderService emailSenderService) {
		return args -> {
			//emailSenderService.sendEmail("a.annahir@gmail.com","test subject", "test body");


			//admin password : ensapay@@2022
			// ajoute d'un administrateur statiquement :
			adminService.registerAdmin(
					new AdminProfileDTO(
							"adminSurname",
							"0661456712",
							"emailAdmin@gmail.com"
					),
					"ensapay@@2022"
			);

			/*clientService.registerClient(
					new ClientProfileDTO(
							ProductType.HSSAB1,
							"clientName1",
							"clientSurname1",
							"0111222333",
							"a.annahir@gmail.com"
					)
			);

			agentService.registerAgent(
					new AgentProfileDTO(
							"agentName1",
							"agentSurname1",
							"0999888777",
							"agentEmail1@email.com",
							null,
							null,
							"EE123465",
							Date.valueOf(LocalDate.of(1990, 5, 30)),
							"addressField1 addressField2 addressField3",
							"0000/11111111",
							"01234567"
					)
			);*/
//			clientService.registerClient(new ClientProfileDTO(ProductType.HSSAB1, "name2", "surname1", "0111222333", "email1@email.com"));
//			clientService.registerClient(new ClientProfileDTO(ProductType.HSSAB1, "name1", "surname1", "0111222333", "email1@email.com"));

		};
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
