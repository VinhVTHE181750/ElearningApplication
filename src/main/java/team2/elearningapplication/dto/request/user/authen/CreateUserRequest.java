package team2.elearningapplication.dto.request.user.authen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import team2.elearningapplication.Enum.EnumTypeGender;
import team2.elearningapplication.Enum.EnumTypeRole;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    @NotBlank
    private String password;
    @NotBlank
    private String email;
    @NotBlank
    private String phone;
//    @NotNull
    private EnumTypeRole role = EnumTypeRole.STUDENT;
    @NotBlank
    private String fullName;
    @NotNull
    private EnumTypeGender gender;
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;


}
