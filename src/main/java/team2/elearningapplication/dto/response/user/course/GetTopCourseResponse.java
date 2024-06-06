package team2.elearningapplication.dto.response.user.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team2.elearningapplication.entity.Course;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GetTopCourseResponse {
    @NotEmpty
    private List<Course> listCourse;
}
