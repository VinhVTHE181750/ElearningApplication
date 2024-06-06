package team2.elearningapplication.dto.response.admin.lesson;

import lombok.*;
import team2.elearningapplication.entity.Course;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetLessonByIdResponse {
    @NotNull
    private int id;
    @NotBlank
    private String name;
    @NotNull
    private int ordNumber;
    @NotNull
    private Course course;
    @NotBlank
    private String linkContent;
    @NotBlank
    private String description;
    @NotNull
    private LocalDateTime createdAt;
    @NotNull
    private boolean isDeleted;
}
