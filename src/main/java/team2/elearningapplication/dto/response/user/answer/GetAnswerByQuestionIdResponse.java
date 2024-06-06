package team2.elearningapplication.dto.response.user.answer;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class GetAnswerByQuestionIdResponse {

    @NotNull
    private int answerId;
    @NotBlank
    private String answerContent;
}

