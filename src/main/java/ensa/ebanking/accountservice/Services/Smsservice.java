package ensa.ebanking.accountservice.Services;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import ensa.ebanking.accountservice.DAO.SmsDao;
import ensa.ebanking.accountservice.DAO.UserDAO;
import ensa.ebanking.accountservice.Entities.Sms;

import ensa.ebanking.accountservice.Entities.User;
import ensa.ebanking.accountservice.Tasks.DeleteSmsFromDb;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;

@Service
public class Smsservice {

	private SmsDao smsDao;
	private UserDAO userDAO;

	public Smsservice(SmsDao smsDao, UserDAO userDAO) {
		this.smsDao = smsDao;
		this.userDAO = userDAO;
	}


	public String otp() {
		int len = 6;
		String numbers = "0123456789";
		Random rndm_method = new Random();

		char[] otp = new char[len];

		for (int i = 0; i < len; i++) {
			otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
		}
		return String.copyValueOf(otp);
	}


	//send message to number
	public int sendsms(String phone) throws IOException {
		String OTP = otp();
		User user = userDAO.findByPhoneNumber(phone);
		String vonageNumber = "212" + phone.substring(1);
		Calendar date = Calendar.getInstance();
		long timeInSecs = date.getTimeInMillis();
		Date date_expiration = new Date(timeInSecs + (3 * 60 * 1000));
		Sms otpSms = new Sms(phone, OTP, user, date_expiration);


		//send code with vonage provider
		VonageClient client = VonageClient.builder().apiKey("82dc50b5").apiSecret("cpgqKACNZWYcKC5K").build();
		TextMessage message = new TextMessage("ENSA PAY",
				vonageNumber,
				"ENSA PAY Verification Code : " + OTP);

		SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);
		if (user != null) {
			if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
				System.out.println("Message sent successfully.");
				smsDao.save(otpSms);
				Timer timer;
				timer = new Timer();
				timer.schedule(new DeleteSmsFromDb(otpSms, smsDao), date_expiration);

				return HttpServletResponse.SC_OK;
			} else {
				System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
				return HttpServletResponse.SC_EXPECTATION_FAILED;
			}
		} else {
			System.out.println("User Not found ");
			return HttpServletResponse.SC_EXPECTATION_FAILED;
		}


	}

	public int verifieOtp(String phone, String code) {
		User user = userDAO.findByPhoneNumber(phone);
		if (user == null) {
			return HttpServletResponse.SC_NOT_FOUND;
		} else {
			Sms sms = smsDao.findSmsByMessageTextAndUser(code, user);
			if(sms==null){
				return HttpServletResponse.SC_NOT_FOUND;}
			else{
				return HttpServletResponse.SC_OK;
			}

		}
	}
}
