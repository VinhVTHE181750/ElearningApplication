package team2.elearningapplication.dto.response.user.authen;

import lombok.*;
import team2.elearningapplication.Enum.EnumTypeGender;
import team2.elearningapplication.Enum.EnumTypeRole;
import team2.elearningapplication.Enum.EnumUserStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetUserByEmailResponse {
    @NotNull
    private int id;
    @NotBlank
    private String username;
    @NotBlank
    private String email;
    @NotBlank
    private String phone;
    @NotNull
    private EnumTypeRole role;
    @NotNull
    private LocalDateTime createdAt;
    @NotBlank
    private String fullName;
    @NotNull
    private EnumTypeGender gender;
    @NotNull
    private LocalDate date_of_birth;
    @NotNull
    private EnumUserStatus status;
}
