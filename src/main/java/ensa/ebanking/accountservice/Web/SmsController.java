package ensa.ebanking.accountservice.Web;



import ensa.ebanking.accountservice.Services.Smsservice;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class SmsController {

    private Smsservice smsservice;

    SmsController(Smsservice smsservice){
        this.smsservice=smsservice;

    }

    @PostMapping("/send_code")
    @ResponseBody
    public String sendmessage(@RequestParam(value = "phone") String number_phone) throws IOException {
        JSONObject js=new JSONObject();
        int status=smsservice.sendsms(number_phone);
        js.put("status",status);

        return js.toString();
    }


    @PostMapping("/otp-verification")
    @ResponseBody
    public String verifyOtp(@RequestParam(value = "phone") String number_phone,@RequestParam(value = "otp") String code) throws IOException {
        JSONObject js=new JSONObject();
        int status=smsservice.verifieOtp(number_phone,code);
        js.put("code",status);
        if(status== HttpServletResponse.SC_NOT_FOUND){
            js.put("status","Ce code a été expire");

        }
        else {
            js.put("status","code valide");
        }

        return js.toString();
    }


}
