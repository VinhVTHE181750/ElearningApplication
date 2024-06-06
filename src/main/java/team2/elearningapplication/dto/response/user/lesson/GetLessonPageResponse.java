package team2.elearningapplication.dto.response.user.lesson;

import lombok.Data;
import team2.elearningapplication.entity.Lesson;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class GetLessonPageResponse {
    @NotEmpty
    private List<Lesson> lessonsList;
    @NotNull
    private int pageNo;
    @NotNull
    private int pageSize;
    @NotNull
    private int totalElements;
    @NotNull
    private int totalPages;
    @NotNull
    private boolean last;
}
