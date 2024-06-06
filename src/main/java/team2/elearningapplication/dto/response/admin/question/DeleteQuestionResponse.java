package team2.elearningapplication.dto.response.admin.question;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeleteQuestionResponse {
    @NotNull
    private int quizID;
    @NotNull
    private int questionID;
}
