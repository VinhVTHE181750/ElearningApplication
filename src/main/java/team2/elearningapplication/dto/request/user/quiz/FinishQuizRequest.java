package team2.elearningapplication.dto.request.user.quiz;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Data
public class FinishQuizRequest {
    @NotBlank
    private String username;
    @NotNull
    private int courseId;
    @NotNull
    private int quizId;
    @NotNull
    private int sessionId;
}
