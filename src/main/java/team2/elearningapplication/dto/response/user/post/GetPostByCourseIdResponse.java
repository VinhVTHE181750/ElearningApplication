package team2.elearningapplication.dto.response.user.post;

import lombok.Data;
import team2.elearningapplication.entity.Post;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class GetPostByCourseIdResponse {
    @NotEmpty
    private List<Post> postList;
}
