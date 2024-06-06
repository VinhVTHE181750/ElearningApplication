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
public class AddLessonRequest {
    @NotBlank
    private String username;
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
}
