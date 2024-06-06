package team2.elearningapplication.dto.request.admin.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddCategoryRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String name;
}
