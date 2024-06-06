package team2.elearningapplication.dto.response.user.question;

import lombok.Data;
import team2.elearningapplication.entity.Question;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class GetQuestionPageResponse {
    @NotEmpty
    private List<Question> questionList;
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
