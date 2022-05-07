package ensa.ebanking.accountservice.Web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import ensa.ebanking.accountservice.Entities.User;
import ensa.ebanking.accountservice.Services.ClientService;
import ensa.ebanking.accountservice.Utilities.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RequestMapping("api/auth/")
@RestController
public class AuthenticationController {
    private final ClientService clientService;

    @Autowired
    public AuthenticationController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(JWTUtil.AUTH_HEADER);
        if(authorizationHeader != null && authorizationHeader.startsWith(JWTUtil.PREFIX)) {
            String refresh_token = authorizationHeader.substring(JWTUtil.PREFIX.length());
            Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            try {
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String phoneNumber = decodedJWT.getSubject();
                User client = clientService.getClient(phoneNumber);
                String access_token = JWT.create()
                        .withSubject(client.getPhoneNumber())
                        .withExpiresAt(new Date(System.currentTimeMillis() + JWTUtil.EXPIRATION_ACCESS_TOKEN))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("phoneNumber", client.getPhoneNumber())
                        .sign(algorithm);

                response.setHeader("access_token", access_token);
            } catch (Exception e){
                response.sendError(FORBIDDEN.value());
            }

        } else {
            throw new RuntimeException("Refresh token missing");
        }
    }
}
