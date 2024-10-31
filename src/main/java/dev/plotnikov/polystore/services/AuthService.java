package dev.plotnikov.polystore.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.plotnikov.polystore.entities.Role;
import dev.plotnikov.polystore.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AuthService {

    private final UserService userService;
    private final Algorithm algorithm;

    public AuthService(UserService userService) {
        this.userService = userService;
        this.algorithm = Algorithm.HMAC256("secret".getBytes());
    }

    public String createRefreshToken(Instant refreshExpirationInstant, String username, HttpServletRequest request) {
        return  JWT.create()
                .withSubject(username)
                .withExpiresAt(Date.from(refreshExpirationInstant))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
    }

    public String createAccessToken(Instant accessExpirationInstant, String refresh_token, HttpServletRequest request) {
        User user = getUserFromToken(refresh_token);
        String username = user.getUsername();
        List<String> roles = user.getRoles().stream().map(Role::getName).toList();
        return createAccessTokenWithUser(accessExpirationInstant, username, roles, request.getRequestURL().toString());
    }
    public String createAccessTokenWithUser(Instant accessExpirationInstant, String username, List<String> roles, String url) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(Date.from(accessExpirationInstant))
                .withIssuer(url)
                .withClaim("roles", roles)
                .sign(algorithm);
    }

    public User getUserFromToken(String refresh_token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refresh_token);
        String username = decodedJWT.getSubject();
        return userService.getUserByUsername(username);
    }

}