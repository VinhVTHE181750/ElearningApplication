package team2.elearningapplication.dto.response.admin.quiz;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team2.elearningapplication.entity.Quiz;

import javax.validation.constraints.NotNull;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FindAllQuizResponse {
    @NotNull
    private List<Quiz> quizList;

}
