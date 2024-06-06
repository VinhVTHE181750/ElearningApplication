package team2.elearningapplication.dto.response.admin.course;

import lombok.*;
import team2.elearningapplication.entity.Category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetCourseByIdResponse {
    @NotNull
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private double price;
    @NotEmpty
    private Category category;
    @NotBlank
    private String linkThumail;
    @NotNull
    private LocalDateTime createAt;
    @NotNull
    private boolean isDeleted;
}
