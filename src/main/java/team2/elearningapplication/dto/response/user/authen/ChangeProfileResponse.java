package team2.elearningapplication.dto.response.user.authen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team2.elearningapplication.Enum.EnumTypeGender;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ChangeProfileResponse {
    @NotBlank
    private String fullName;
    @NotBlank
    private String phoneNum;
    @NotNull
    private EnumTypeGender gender;

}
