package ensa.ebanking.accountservice.Helpers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class EmailHelper {

    public EmailHelper(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private JavaMailSender mailSender;

    public JSONObject parseJsonFile(String path) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("ensapay@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

        System.out.println("email sent to : " + toEmail);
        System.out.println("subject : " + subject);
        System.out.println("body : " + body);
    }
}