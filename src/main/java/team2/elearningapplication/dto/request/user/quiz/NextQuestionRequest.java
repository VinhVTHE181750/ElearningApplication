package team2.elearningapplication.dto.request.user.quiz;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class NextQuestionRequest {
    @NotNull
    private int preQuestionId;
    @NotNull
    private int ordQuestion;
    @NotBlank
    private String username;
    @NotNull
    private int sessionId;
    @NotNull
    private int quizId;
    private int answerId;
}
