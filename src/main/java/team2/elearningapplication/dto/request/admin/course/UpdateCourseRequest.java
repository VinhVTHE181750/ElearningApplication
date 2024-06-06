package team2.elearningapplication.dto.request.admin.course;

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
public class UpdateCourseRequest {
        @NotBlank
        private String username;
        @NotNull
        private int courseID;
        @NotBlank
        private String name;
        @NotBlank
        private String description;
        @NotNull
        private double price;
        @NotBlank
        private String link_thumnail;
        @NotNull
        private int categoryID;
        @NotNull
        private boolean deleted;
}
