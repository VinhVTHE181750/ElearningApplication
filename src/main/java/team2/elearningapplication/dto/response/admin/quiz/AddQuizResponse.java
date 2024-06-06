package team2.elearningapplication.dto.response.admin.quiz;

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
public class AddQuizResponse {
    @NotNull
    private int quizID;
    @NotNull
    private int lessonID;
    @NotBlank
    private String lessonName;
}
