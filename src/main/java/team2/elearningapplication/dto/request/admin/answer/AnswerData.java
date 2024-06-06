package team2.elearningapplication.dto.request.admin.answer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnswerData {
    @NotBlank
    private String username;
    @NotNull
    private int questionID;
    @NotBlank
    private String answerName;
    @NotNull
    private boolean isCorrect;
}
