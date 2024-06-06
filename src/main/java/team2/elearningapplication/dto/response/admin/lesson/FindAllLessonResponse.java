package team2.elearningapplication.dto.response.admin.lesson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team2.elearningapplication.entity.Lesson;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FindAllLessonResponse {
    @NotBlank
    private String message;
    @NotEmpty
    private List<Lesson> listLesson;
}
