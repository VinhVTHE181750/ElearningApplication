package team2.elearningapplication.dto.request.user.course;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EnrollCourseRequest {
    @NotNull
    private int courseId;
    @NotBlank
    private String username;

}
