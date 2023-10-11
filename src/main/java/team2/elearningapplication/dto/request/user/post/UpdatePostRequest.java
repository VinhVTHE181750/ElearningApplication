package team2.elearningapplication.dto.request.user.post;

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
public class UpdatePostRequest {
    @NotBlank
    private String email;
    @NotNull
    private int lessonID;
    @NotBlank
    private String content;
}
