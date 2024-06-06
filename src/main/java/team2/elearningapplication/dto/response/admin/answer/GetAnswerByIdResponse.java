package team2.elearningapplication.dto.response.admin.answer;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetAnswerByIdResponse {
    @NotNull
    private int id;
    @NotBlank
    private String answerContent;
    @NotNull
    private boolean isCorrect;
    @NotNull
    private int questionId;
    @NotNull
    private boolean isDeleted;
    @NotBlank
    private String createdBy;
    @NotBlank
    private String updatedBy;
}
