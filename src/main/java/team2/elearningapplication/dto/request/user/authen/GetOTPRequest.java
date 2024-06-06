package team2.elearningapplication.dto.request.user.authen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetOTPRequest {
    @NotBlank
    private String email;
    @JsonIgnore
    private boolean isCreate;
}
