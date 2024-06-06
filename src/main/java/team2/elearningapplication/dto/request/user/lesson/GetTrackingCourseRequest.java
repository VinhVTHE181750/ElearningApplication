package team2.elearningapplication.dto.request.user.lesson;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class GetTrackingCourseRequest {
    @NotNull
    private int courseId;
    @NotBlank
    private String username;
}
