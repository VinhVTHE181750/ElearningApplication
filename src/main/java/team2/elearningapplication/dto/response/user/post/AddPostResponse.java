package team2.elearningapplication.dto.response.user.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddPostResponse {
    @NotBlank
    private int postID;
    @NotNull
    private String email;
    @NotNull
    private int lessonID;
    @NotBlank
    private String content;
    @NotNull
    private LocalDateTime createdAt;
}
