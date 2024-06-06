package team2.elearningapplication.dto.response.admin.question;

import lombok.*;
import team2.elearningapplication.Enum.EnumQuestionType;
import team2.elearningapplication.entity.Answer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetQuestionByIdResponse {
    @NotNull
    private int id;
    @NotBlank
    private String questionName;
    @NotNull
    private EnumQuestionType questionType;
    @NotEmpty
    private List<Answer> answerList;
    @NotNull
    private int quizID;
    @NotNull
    private boolean deleted;
}
