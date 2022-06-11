package ensa.ebanking.accountservice.Filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import ensa.ebanking.accountservice.Enums.Role;
import ensa.ebanking.accountservice.Utilities.JWTUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class CustomTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().startsWith("/api/account/client")){
            String authorizationHeader = request.getHeader(JWTUtil.AUTH_HEADER);
            if(authorizationHeader != null && authorizationHeader.startsWith(JWTUtil.PREFIX)) {
                String token = authorizationHeader.substring(JWTUtil.PREFIX.length());
                Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                try {
                    DecodedJWT decodedJWT = verifier.verify(token);

                    String claim_string = decodedJWT.getClaim("roles").toString()
                            .replace("[", "")
                            .replace("]", "")
                            .replace("\"", "");

                    if(!claim_string.equals("ROLE_" + Role.AGENT.name)){
                        throw new Exception("insuffisant role");
                    }

                    filterChain.doFilter(request, response);
                } catch (Exception e){
                    response.sendError(FORBIDDEN.value());
                }

            } else {
                filterChain.doFilter(request, response);
            }
        }else {
            filterChain.doFilter(request, response);
        }
    }
}
