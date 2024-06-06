package team2.elearningapplication.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JWTResponse {
    private String accessToken;
    private String refreshToken;
    private String message;
}
