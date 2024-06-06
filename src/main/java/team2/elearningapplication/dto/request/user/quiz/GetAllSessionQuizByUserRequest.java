package team2.elearningapplication.dto.request.user.quiz;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GetAllSessionQuizByUserRequest {
    @NotBlank
    private String username;

}
