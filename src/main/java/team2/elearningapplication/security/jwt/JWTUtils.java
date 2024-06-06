package team2.elearningapplication.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import team2.elearningapplication.security.UserDetailsImpl;

import java.util.Date;
import java.util.stream.Collectors;

;

@Component
public class JWTUtils {


    public String generateAccessToken(UserDetailsImpl userDetails) {
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + 86400000))
                .withClaim("userInfo", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String generateRefreshToken(UserDetailsImpl userDetails) {
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + 5 * 86400000))
                .sign(algorithm);
    }

}
