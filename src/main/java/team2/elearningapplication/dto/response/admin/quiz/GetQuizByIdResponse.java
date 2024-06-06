package team2.elearningapplication.dto.response.admin.quiz;

import lombok.*;
import team2.elearningapplication.entity.Lesson;
import team2.elearningapplication.entity.Question;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetQuizByIdResponse {
    @NotNull
    private int id;
    @NotNull
    private Lesson lesson;
    @NotBlank
    private String name;
    @NotNull
    private Question question;
    @NotNull
    private boolean isDeleted;
}
