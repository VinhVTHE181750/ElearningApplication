package team2.elearningapplication.dto.response.admin.dashboard;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GetTotalUserResponse {
    @NotNull
    private int totalUser;
}
