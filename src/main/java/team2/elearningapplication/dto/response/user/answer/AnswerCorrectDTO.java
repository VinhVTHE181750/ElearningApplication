package team2.elearningapplication.dto.response.user.answer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnswerCorrectDTO {
    private int id;
    private String answerContent;
    private boolean isCorrect;
    private int questionId;
}
