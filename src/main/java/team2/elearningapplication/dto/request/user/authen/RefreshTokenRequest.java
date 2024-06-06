package team2.elearningapplication.dto.request.user.authen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RefreshTokenRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String refreshToken;
}
