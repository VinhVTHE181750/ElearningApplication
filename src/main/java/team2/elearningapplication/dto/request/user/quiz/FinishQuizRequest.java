package team2.elearningapplication.dto.request.user.quiz;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    @NotEmpty
    private List<Integer> answerIdList;
}
