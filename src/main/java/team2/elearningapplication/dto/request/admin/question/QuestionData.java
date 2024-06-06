package team2.elearningapplication.dto.request.admin.question;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team2.elearningapplication.Enum.EnumQuestionType;
import team2.elearningapplication.entity.Answer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionData {
    @NotBlank
    private String username;
    @NotNull
    private int quizID;
    @NotNull
    private EnumQuestionType questionType;
    @NotBlank
    private String questionName;
    @NotEmpty
    private List<Answer> listAnswer;
}
