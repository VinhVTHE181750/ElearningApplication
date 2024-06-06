package team2.elearningapplication.dto.response.admin.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnrollCouseResponse {
    @NotNull
    private int userID;
    @NotNull
    private int courseID;
    @NotNull
    private int ordLessonCurrent;
    @NotNull
    private int paymentID;

}
