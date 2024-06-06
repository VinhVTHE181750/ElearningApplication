package team2.elearningapplication.dto.response.admin.category;

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
public class AddCategoryResponse {
    @NotNull
    private int categoryID;
    @NotBlank
    private String categoryName;
    @NotBlank
    private String createdBy;
    @NotBlank
    private String updatedBy;

}
