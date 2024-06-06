package team2.elearningapplication.dto.response.user.authen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserResponseDTO {
    private String username;
    private String email;
    private LocalDateTime createdAt;
}
