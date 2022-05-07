package ensa.ebanking.accountservice.Filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import ensa.ebanking.accountservice.Enums.Role;
import ensa.ebanking.accountservice.Utilities.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class CustomTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/api/account/client/hello-world")){
            String authorizationHeader = request.getHeader(JWTUtil.AUTH_HEADER);
            if(authorizationHeader != null && authorizationHeader.startsWith(JWTUtil.PREFIX)) {
                String token = authorizationHeader.substring(JWTUtil.PREFIX.length());
                Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                try {
                    DecodedJWT decodedJWT = verifier.verify(token);
                    Claim claims = decodedJWT.getClaim("roles");

                    String claim_string = claims.toString()
                            .replace("[", "")
                            .replace("]", "")
                            .replace("\"", "");

                    if(!claim_string.equals("ROLE_" + Role.ROLE_AGENT.name)){
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
