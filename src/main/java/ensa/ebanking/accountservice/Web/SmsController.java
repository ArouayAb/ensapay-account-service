package ensa.ebanking.accountservice.Web;



import ensa.ebanking.accountservice.Services.Smsservice;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("verification")
public class SmsController {

    private Smsservice smsservice;

    SmsController(Smsservice smsservice){
        this.smsservice=smsservice;
    }

    @PostMapping("/send-code")
    @ResponseBody
    public ResponseEntity<String> sendmessage(@RequestBody String jsonBody) throws IOException {
        String phone = (String) new JSONObject(jsonBody).get("phone");
        int status = smsservice.sendsms(phone);

        if (status == HttpServletResponse.SC_EXPECTATION_FAILED) {
            return ResponseEntity.status(500).build();
        }
        else {
            return ResponseEntity.status(200).build();
        }
    }


    @PostMapping("/otp-verification")
    @ResponseBody
    public ResponseEntity<String> verifyOtp(@RequestBody String jsonBody) throws IOException {
        String phone = (String) new JSONObject(jsonBody).get("phone");
        String code = (String) new JSONObject(jsonBody).get("otp");


        int status = smsservice.verifieOtp(phone,code);
        if(status == HttpServletResponse.SC_NOT_FOUND) {
            return ResponseEntity.status(500).build();
        }
        else {
            return ResponseEntity.status(200).build();
        }


    }


}
