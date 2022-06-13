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
@EnableAutoConfiguration
public class AccountServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(AccountServiceApplication.class, args);

	}

	@Bean
	CommandLineRunner run(
			BankAccountHelper bankAccountHelper,
			CreancierDAO creancierDAO,
			ServiceProviderDAO serviceProviderDAO,
			CreanceDAO creanceDAO,
			ClientProfileDAO clientProfileDAO,
			ClientService clientService,
			AgentService agentService,
			AdminService adminService,
			CMIService cmiService,
			EmailHelper emailHelper) {
		return args -> {
//			emailHelper.sendEmail("enter-your-test-mail-here@gmail.com","test subject", "test body");

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

			clientService.registerClient(
					new ClientProfileDTO(
							ProductType.HSSAB1,
							"clientName1",
							"clientSurname1",
							"0111222333",
							"clientEmail1@gmail.com"
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
			);

			cmiService.saveStaticDataToDB();

			creanceDAO.save(new Creance(
					1L,
					LocalDateTime.of(2022, 10, 1,12,20,16),
					CreanceStatus.UNPAID,
					clientProfileDAO.findById(1L).get(),
					creancierDAO.findById(1L).get(),
					12.53D
			));
			creanceDAO.save(new Creance(
					2L,
					LocalDateTime.of(2022, 9, 1,2,15,3),
					CreanceStatus.UNPAID,
					clientProfileDAO.findById(1L).get(),
					creancierDAO.findById(1L).get(),
					200D
			));
			creanceDAO.save(new Creance(
					3L,
					LocalDateTime.of(2022, 9, 1,2,15,3),
					CreanceStatus.PENDING,
					clientProfileDAO.findById(1L).get(),
					creancierDAO.findById(1L).get(),
					200D
			));
			creanceDAO.save(new Creance(
					4L,
					LocalDateTime.of(2022, 9, 1,2,15,3),
					CreanceStatus.PENDING,
					clientProfileDAO.findById(1L).get(),
					creancierDAO.findById(1L).get(),
					200D
			));
			creanceDAO.save(new Creance(
					5L,
					LocalDateTime.of(2022, 9, 1,2,20,10),
					CreanceStatus.PENDING,
					clientProfileDAO.findById(1L).get(),
					creancierDAO.findById(1L).get(),
					200D
			));

//			clientService.registerClient(new ClientProfileDTO(ProductType.HSSAB1, "name2", "surname1", "0111222333", "email1@email.com"));
//			clientService.registerClient(new ClientProfileDTO(ProductType.HSSAB1, "name1", "surname1", "0111222333", "email1@email.com"));

		};
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
