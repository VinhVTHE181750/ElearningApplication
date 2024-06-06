package team2.elearningapplication.dto.request.admin.lesson;

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
public class UpdateLessonRequest {
    @NotBlank
    private String username;

    @NotNull
    private int lessonID;
    @NotBlank
    private String lessonName;
    @NotNull
    private int ordNumber;
    @NotNull
    private int courseID;
    @NotBlank
    private String linkContent;
    @NotBlank
    private String description;
    @NotNull
    private boolean deleted;

}
