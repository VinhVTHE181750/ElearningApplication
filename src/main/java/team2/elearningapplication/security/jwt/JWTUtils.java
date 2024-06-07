package team2.elearningapplication.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import team2.elearningapplication.security.UserDetailsImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

@Component
public class JWTUtils {


    public String generateAccessToken(UserDetailsImpl userDetails) {
        String secret = null;
        try {
            secret = Files.readString(Path.of("secret.txt"));
        } catch (IOException e) {
            throw new SecurityException(e);
        }
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + 86400000))
                .withClaim("userInfo", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .sign(algorithm);
    }

    public String generateRefreshToken(UserDetailsImpl userDetails) {
        String secret = null;
        try {
            secret = Files.readString(Path.of("secret.txt"));
        } catch (IOException e) {
            throw new SecurityException(e);
        }
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + 5 * 86400000))
                .sign(algorithm);
    }

}
