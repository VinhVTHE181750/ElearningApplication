package team2.elearningapplication.dto.request.admin.quiz;

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
public class UpdateQuizRequest {
    @NotBlank
    private String username;
    @NotNull
    private int quizID;
    @NotBlank
    private String quizName;
    @NotNull
    private int lessonID;
    @NotBlank
    private String lessonName;
    @NotNull
    private boolean deleted;

}
